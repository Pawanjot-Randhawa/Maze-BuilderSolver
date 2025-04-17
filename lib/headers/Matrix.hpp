#ifndef MATRIX_HPP
#define MATRIX_HPP

#include <queue>
#include <cmath>
#include <vector>
#include <limits>
#include <utility>
#include <algorithm>
#include <unordered_map>
#include <iostream>


#include "Bit.hpp"
#include "Cell.hpp"
#include "Logger.h"
#include "Weighted_Node.hpp"

template <class T>
class Matrix {
private:

    T** matrix{nullptr};
    
    int rows{};
    int cols{};
    T startValue;
    T goalValue;
    T wallValue;

    
    std::pair<int, int> startingPosition{};
    std::vector<std::pair<int, int>> goals{};
    std::vector<std::pair<int, int>> path{};
    
    Matrix() = default;
    
    void InitializeMatrix();
    void DestructMatrix();
    bool isValidPosition(int& row, int& col);
    bool isBlocked(int& row, int& col);
    bool isGoal(int& row, int& col);

public:    
    static Matrix& GetInstance();
    Matrix(const Matrix&) = delete;
    Matrix& operator=(const Matrix&) = delete;

    void SetDimensions(const int& rows, const int& cols);
    void Initialize(
      const int& rows, 
      const int& cols, 
      const T& startValue, 
      const T& goalValue,
      const T& wallValue
    );
    
    T** GetMatrix();
    std::vector<std::pair<int, int>> GetPath() const;

    void Clear();
    void Print();
    void SolveWith(const std::string& str);
    
    void SetWall(const T& value);
    void Set(const int& row, const int& col, const T& value);

    void AStar();
    void Dijkstra();
    void FindPath(std::vector<std::vector<Cell>>& cells, std::pair<int, int> destination);

    ~Matrix();
};

#endif