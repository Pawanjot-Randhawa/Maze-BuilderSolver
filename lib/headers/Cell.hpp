#ifndef CELL_HPP
#define CELL_HPP
#include <limits>

struct Cell {
  int row{};
  int col{};

  // f
  double totalCost {std::numeric_limits<double>::infinity()};
  // g
  double currentCost {std::numeric_limits<double>::infinity()};
  
  // h
  double heuristicCost{0};

  Cell(const double& cost, const int& i, const int& j)
  : currentCost(cost), row(i), col(j) {}

  Cell(const double& cost, int& i, int& j)
  : totalCost(cost), row(i), col(j) {}

  bool operator<(const Cell& other) const {
    return this->heuristicCost > other.heuristicCost;
  }

  Cell() = default;
};

#endif