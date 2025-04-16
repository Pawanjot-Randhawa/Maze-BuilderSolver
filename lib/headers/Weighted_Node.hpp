#ifndef WEIGHTED_NODE_HPP
#define WEIGHTED_NODE_HPP
#include <utility>

struct WeightedNode {
    int row{};
    int col{};
  
    double cost{0};
  

    
    WeightedNode(const double& cost, const int& i, const int& j)
    : cost(cost), row(i), col(j) {}
  
    WeightedNode(const double& cost, int& i, int& j)
    : cost(cost), row(i), col(j) {}



    WeightedNode(const int& i, const int& j)
    : row(i), col(j) {}
  
    WeightedNode(int& i, int& j)
    : row(i), col(j) {}
  
    WeightedNode(const std::pair<int ,int>& pair)
    : row(pair.first), col(pair.second) {}

    WeightedNode(std::pair<int ,int>& pair)
    : row(pair.first), col(pair.second) {}

    

    WeightedNode(const double& cost, const std::pair<int ,int>& pair)
    : cost(cost), row(pair.first), col(pair.second) {}

    WeightedNode(const double& cost, std::pair<int ,int>& pair)
    : cost(cost), row(pair.first), col(pair.second){}




    bool operator<(const WeightedNode& other) const {
      return this->cost > other.cost;
    }

    bool operator==(const WeightedNode& other) const {
        return this->row == other.row 
        && this->col == other.col 
        && this->cost == other.cost;
    }

    std::pair<int, int> GetPosition() const {
      return std::make_pair(this->row, this->col);
    }
  
    WeightedNode() = default;
};

namespace std {
  template <>
  struct hash<WeightedNode> {
    std::size_t operator()(const WeightedNode& node) const {
      return std::hash<int>{}(node.row) ^ (std::hash<int>{}(node.col) << 1);
    }
  };
};

#endif  