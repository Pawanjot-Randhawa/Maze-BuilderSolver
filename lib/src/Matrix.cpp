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
    this->goals.clear();
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
      this->originalPlayerPosition = this->startingPosition;
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
  const T& wallValue,
  const T& pathValue,
  const T& blockedValue
) {
  this->startValue = startValue;
  this->goalValue = goalValue;
  this->wallValue = wallValue;
  this->pathValue = pathValue;
  this->blockedValue = blockedValue;

  if(this->matrix) {
    this->DestructMatrix();
  }
  this->Clear();
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
void Matrix<T>::SolveWith(const std::string& str) {
  if(str == "AStar") {
    logger.Debug("Using AStar");
    this->AStar();
  } else if (str == "Dijkstra") {
    logger.Debug("Using Dijkstra");
    this->Dijkstra();
  } else {
    logger.Debug("using custom BlockNdPath");
    this->BlockNdPath();
    this->FixMaze();
  }
}


template class Matrix<int>;

