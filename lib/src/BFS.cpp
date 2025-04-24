#include "../headers/Matrix.hpp"

template <class T>
void Matrix<T>::BFS() {
    using Position = std::pair<int, int>;
    std::unordered_set<WeightedNode> visited{};
    std::queue<Position> queue{};

    const Position directions[] {
        {1, 0},
        {-1, 0},
        {0, 1},
        {0, -1}
    };

    queue.emplace(this->startingPosition);
    while(!queue.empty()) {
        Position current = queue.front();
        queue.pop();

        if(visited.find(current) != visited.end()) {
            // Already visited this one...
            continue;
        }
        visited.insert(current);

        this->path.push_back(current);
        if(this->isGoal(current.first, current.second)) {
            // Empty the queue
            while(!queue.empty()) {
                queue.pop();
            }
            return;
        }

        // Expand the queue to the other directions
        for(const Position& dir : directions) {
            const int newX{current.first + dir.first};
            const int newY{current.second + dir.second};   
            if(!this->IsInvalid(newX, newY)) {
                // If this is a valid position, add it to the queue
                queue.emplace(newX, newY);
            }
        }
    }

    // there is no solution if it goes here
    // empty the path
    this->path.clear();
}

template class Matrix<int>;