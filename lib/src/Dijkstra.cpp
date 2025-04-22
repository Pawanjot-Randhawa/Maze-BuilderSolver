#include "../headers/Matrix.hpp"

template <class T>
void Matrix<T>::Dijkstra() {
  using Position = std::pair<int, int>;
  const int baseCost{0};

  const Position directions[] = 
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

  Position chosenGoal{};

  std::unordered_map<WeightedNode, double> distances{};
  std::unordered_map<WeightedNode, WeightedNode> previous{};

  for(int r = 0; r < this->rows; ++r) {
    for(int c = 0; c < this->cols; ++c) {
      distances[{r,c}] = std::numeric_limits<double>::infinity();
    }
  }

  distances[this->startingPosition] = baseCost;

  std::priority_queue<WeightedNode> pq{};
  pq.push({baseCost, this->startingPosition});

  while(!pq.empty()) {
    WeightedNode current = pq.top();
    pq.pop();

    int row{current.row};
    int col{current.col};

    // found a goal, get out of here
    if(this->isGoal(row, col)) {
      chosenGoal.first = row;
      chosenGoal.second = col;
      break;
    }

    for(const Position& dir : directions) {
      int newRow{row + dir.first};
      int newCol{col + dir.second};

      if (!this->isValidPosition(newRow, newCol)) continue;
      if (this->isBlocked(newRow, newCol)) continue;

      double weight = static_cast<double>(matrix[newRow][newCol]);
        double newDist = distances[{row, col}] + weight;

      if (newDist < distances[{newRow, newCol}]) {
        distances[{newRow, newCol}] = newDist;
        previous[{newRow, newCol}] = {row, col};
        pq.push(WeightedNode(newDist, {newRow, newCol}));
      }
    }
  }

  // Re-create the path
  Position current = chosenGoal;
  if (distances[current] == std::numeric_limits<double>::infinity()) {
    std::cout << "No path found!" << std::endl;
    return;
  }

  while(current != this->startingPosition) {
    this->path.push_back(current);
    current = previous[current].GetPosition();
  }
  this->path.push_back(this->startingPosition);

  std::reverse(this->path.begin(), this->path.end());
}

template class Matrix<int>;