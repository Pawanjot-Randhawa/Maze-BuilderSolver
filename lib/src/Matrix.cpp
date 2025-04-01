#include "../headers/Matrix.hpp"

template <class T>
void Matrix<T>::InitializeMatrix() {
    this->DestructMatrix();
    
    this->matrix = new int*[this->rows];
    for(int i = 0; i < this->rows; ++i) {
        this->matrix[i] = new int[this->cols];
    }
}


template <class T>
Matrix<T>& Matrix<T>::GetInstance() {
  static Matrix instance;
  return instance;    
}

template <class T>
void Matrix<T>::DestructMatrix() {
    if(this->matrix) {
        for(int i = 0; i < this->rows; ++i) {
            if(this->matrix[i]) {
                delete[] this->matrix[i];
                this->matrix[i] = nullptr;
            }
        }
        delete[] this->matrix;
        this->matrix = nullptr;
    }
}


template <class T>
void Matrix<T>::SetDimensions(const int& rows, const int& cols) {
    this->rows = rows;
    this->cols = cols;
}


template <class T>
T** Matrix<T>::GetMatrix() {
    // CAN RETURN NULLPTR
    return this->matrix;
}


template <class T>
std::vector<std::pair<int, int>> Matrix<T>::GetPath() const {
  return this->path;
}


template <class T>
void Matrix<T>::Clear() {
    this->DestructMatrix();
    this->path.clear(); 
}


template <class T>
void Matrix<T>::Set(const int& row, const int& col, const T& value) {
    if(!this->matrix) {
        this->InitializeMatrix();
    }
    if(value == this->startValue) {
      std::string str{"Starting Position" + std::to_string(row) + ", " + std::to_string(col)};
      logger.Info("Starting Position");
      this->startingPosition = {row, col};
    }
    if(value == this->goalValue) {
      this->goals.push_back(std::make_pair(row, col));
    }

    this->matrix[row][col] = value;
}


template <class T>
Matrix<T>::~Matrix() {
  std::cout << "Clearing ~Matrix()\n\n";
  this->DestructMatrix();
}


template <class T>
void Matrix<T>::Initialize(
  const int& rows, 
  const int& cols, 
  const T& startValue, 
  const T& goalValue,
  const T& wallValue
) {
  this->startValue = startValue;
  this->goalValue = goalValue;
  this->wallValue = wallValue;

  if(this->matrix) {
    this->DestructMatrix();
  }
    
  this->SetDimensions(rows, cols);
  this->InitializeMatrix();
}


template <class T>
void Matrix<T>::Print() {
  std::cout << "Printing the Maze\n";
  for(int i = 0; i < this->rows; ++i) {
    for(int j = 0; j < this->cols; ++j) {
      std::cout << this->matrix[i][j] << " ";
    }
    std::cout << "\n";
  }
  std::cout << "\n\n";
}


template <class T>
bool Matrix<T>::isValidPosition(int& row, int& col) {
  return (row >= 0) 
  && (row < this->rows) 
  && (col >= 0) 
  && (col < this->cols);
}


template <class T>
bool Matrix<T>::isBlocked(int& row, int& col) {
  return this->matrix[row][col] == this->wallValue;
}


template <class T>
bool Matrix<T>::isGoal(int& row, int& col) {
  return (std::any_of(this->goals.begin(), this->goals.end(), [&](const std::pair<int, int>& goal) {
    return goal.first == row && goal.second == col;
  }));
}


template <class T>
void Matrix<T>::FindPath(
  std::vector<std::vector<Cell>>& cells, 
  std::pair<int, int> destination) 
{
  int row = destination.first;
  int col = destination.second;

  while(!(cells[row][col].row == row && cells[row][col].col == col)) {
    this->path.push_back(std::make_pair(row, col));
    int tmpRow = cells[row][col].row;
    int tmpCol = cells[row][col].col;
    
    row = tmpRow;
    col = tmpCol;
  }

  this->path.push_back(std::make_pair(row, col));
  std::reverse(this->path.begin(), this->path.end());
}



template <class T>
void Matrix<T>::AStar() {

  auto calculateHeuristic = [&](int& row, int& col) {
    double minH = std::numeric_limits<double>::infinity();
    for (const auto& pair : this->goals) {
      double h = std::hypot(row - pair.first, col - pair.second);
      minH = std::min(minH, h);
    }
    return minH;
  };

  const std::pair<int, int> directions[] = 
  {
    {0,1},
    {0,-1},
    {1,0},
    {-1,0},
    {1,1},
    {1,-1},
    {-1,1},
    {-1,-1}
  };
  // Define the visited cells as none currently
  std::vector<std::vector<Bit>> closedList(
    this->rows, 
    std::vector<Bit>(
      this->cols, 
      Bit(false)
    )
  );

  // Define the cell details
  std::vector<std::vector<Cell>> cellDetails(
    this->rows,
    std::vector<Cell>(
      this->cols 
    )
  );

  int i = this->startingPosition.first;
  int j = this->startingPosition.second;

  // Set the details of the starting Cell
  cellDetails[i][j].totalCost = 0.0;
  cellDetails[i][j].currentCost = 0.0;   
  cellDetails[i][j].heuristicCost = 0.0; 

  cellDetails[i][j].row = i; 
  cellDetails[i][j].col = j; 

  std::priority_queue<Cell> openList;
  openList.push(Cell(0.0, i, j));
  Bit foundDestination{false};

  while(!openList.empty()) {
    Cell current = openList.top();
    openList.pop();

    i = current.row;
    j = current.col;
    closedList[i][j].flag = 1;

    for(const auto& dir : directions) {
      int ni = i + dir.first;
      int nj = j + dir.second;

      if(this->isValidPosition(ni, nj) 
        && !this->isBlocked(ni, nj)
        && closedList[ni][nj].flag == 0) {
          if(this->isGoal(ni,nj))  {
            cellDetails[ni][nj].row = i;
            cellDetails[ni][nj].col = j;
            this->FindPath(cellDetails, std::make_pair(ni, nj));
            return;
          }
          else {
            double g = cellDetails[ni][nj].currentCost + 1.0;
            double h = calculateHeuristic(ni, nj);
            double f = g + h;

            if(cellDetails[ni][nj].totalCost 
              == std::numeric_limits<double>::infinity() 
              || f < cellDetails[ni][nj].heuristicCost) 
            {
              openList.push(Cell(f, ni, nj));

              cellDetails[ni][nj].totalCost = f;
              cellDetails[ni][nj].currentCost = g;
              cellDetails[ni][nj].heuristicCost = h;
              cellDetails[ni][nj].row = i;
              cellDetails[ni][nj].col = j;
            }
          } 
        }
    }
  }
}


template class Matrix<int>;

