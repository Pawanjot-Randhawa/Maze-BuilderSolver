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
Bit Matrix<T>::IsInvalid(int row, int col) {
    int r{row};
    int c{col};

    return static_cast<Bit>((!this->isValidPosition(r, c) || this->isBlocked(r, c)));
}




template <class T>
Bit Matrix<T>::ShouldBacktrackPosition(int row, int col) {
    int r{row};
    int c{col};

    return static_cast<Bit>((!this->isValidPosition(r, c) || this->startValue < this->matrix[r][c]));
}



template <class T>
void Matrix<T>::BlockNdPath() {

    int xP{0};
    int yP{0};
    
    // Set the player's current position
    for(int i = 0; i < this->rows; ++i) {
        for(int j = 0; j < this->cols; ++j) {
            if(this->matrix[i][j] == this->startValue) {
            xP = i;
            yP = j;
            }
        }
    }
    this->path.push_back(std::make_pair(xP, yP));

    if(this->isDone()) {
        this->FixMaze();
        return;
    }

    // if player is surrounded by walls, maze is impossible
    if(this->IsInvalid(xP, yP + 1)  
    && this->IsInvalid(xP, yP - 1)
    && this->IsInvalid(xP + 1, yP)
    && this->IsInvalid(xP - 1, yP)
    ) {
        logger.Debug("Maze is impossible from BlockNdPath gg");
        this->Clear();
        return;
    }


    std::vector<std::pair<int, int>> directions = {
        {0, -1}, {0, 1}, {-1, 0}, {1, 0}
    };
    
    for (auto& dir : directions) {
        int newX = xP + dir.first;
        int newY = yP + dir.second;
    
        // If we can go in DIR direction, go there
        if (this->validRow(newX) && this->validCol(newY) &&
            this->matrix[xP][yP] > this->matrix[newX][newY] &&
            !this->isDone()) {
    
            this->matrix[newX][newY] = this->startValue;
            this->matrix[xP][yP] = this->blockedValue;
    
            return BlockNdPath();
        }
    }
    
    if (this->ShouldBacktrackPosition(xP + 1, yP) &&
        this->ShouldBacktrackPosition(xP - 1, yP) &&
        this->ShouldBacktrackPosition(xP, yP + 1) &&
        this->ShouldBacktrackPosition(xP, yP - 1) &&
        !this->isDone()) {
    
        this->matrix[xP][yP] = this->wallValue;
        this->matrix[this->originalPlayerPosition.first][this->originalPlayerPosition.second] = this->startValue;
    
        replaceSeven();
        BlockNdPath();
        return;
    }
}


template <class T>
void Matrix<T>::FixMaze() {

    for (const auto& dir : this->path) {
        if (dir == this->startingPosition) {
            this->matrix[dir.first][dir.second] = this->startValue;
        }
        else {
            this->matrix[dir.first][dir.second] = this->pathValue;
        }
    }
    this->matrix[this->path.back().first][this->path.back().second] = this->goalValue;
}


template class Matrix<int>;