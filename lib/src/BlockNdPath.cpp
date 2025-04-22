#include "../headers/Matrix.hpp"


template <class T>
void Matrix<T>::replaceSeven() {
  
  for(int i = 0; i < this->rows; ++i) {
    for(int j = 0; j < this->cols; ++j) {
      if(this->matrix[i][j] == this->blockedValue) {
        this->matrix[i][j] = 0;
      }
    }
  }
}


template <class T>
Bit Matrix<T>::validRow(int& row) {
  return Bit((row >= 0 && row < this->rows));
}


template <class T>
Bit Matrix<T>::validCol(int& col) {
  return Bit((col < this->cols && col >= 0));
}




template <class T>
void Matrix<T>::BlockNdPath() {

    int row = 5;
    int col = 5;
    if(this->validCol(col)) {
        std::cout << "hi";
    } else {
        std::cout << "bye";
    }
  
}


template class Matrix<int>;