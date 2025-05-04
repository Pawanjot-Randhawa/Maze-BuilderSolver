#include "../headers/Matrix.hpp"

template <class T>
double Matrix<T>::eval(std::pair<int,int> start, double g_n) {
    /**
    Returns the total cost for the node 
    
    Args:
        Start
            The current node you are at
        Goal
            The ending node desired to reach at the end
    
    Returns:
        the cost so far + the estimation of the remaining
    */
    // f(n) = Cost so far + Heuristic
    // f(n) = g(n) + h(n)
    //   g(n) = cost from start to node n  
    //       - THE COST SO FAR
    //   h(n) = estimate from node n to goal
    //       - THE ESTIMATED COST REMAINING    
    return g_n + this->euclideanDistance(start);
}



template <class T>
double Matrix<T>::euclideanDistance(std::pair<int, int> start) {
    /**
    Returns the distance between 2 nodes in euclidean space

    Args:
        Start:
            The starting point 

    Returns:
        the distance between b and a
    */
    double minH = std::numeric_limits<double>::infinity();
    for (const auto& pair : this->goals) {
        double h = std::hypot(start.first - pair.first, start.second - pair.second);
        minH = std::min(minH, h);
    }
    return minH;
}



template <class T>
void Matrix<T>::IDAStar() {
    /**
    Finds and returns the most optimal path and evaluated cost 
    between a starting point and a goal

    Uses:
        Start: Vertex
            - The vertex to begin with
        Goal: Vertex
            - The vertex you want to go to

    Returns
        1. None, if the goal is impossible to reach
        2. (cost, path), a tuple that contains the cost of reaching the path and the actual path itself
    */
    using Position = std::pair<int, int>;
    const Position directions[] {
        {0,1},
        {0,-1},
        {1,0},
        {-1,0},
        {1,1},
        {1,-1},
        {-1,1},
        {-1,-1}
    };
    
    // this is equal to the f_n since g_n would be 0 right now
    double threshold = this->euclideanDistance(this->startingPosition);
    
    // Iterative Deepening
    while(true) {
        std::stack<std::pair<Position, double>> stack{};
        stack.emplace(this->startingPosition, 0);

        double min_f_score{std::numeric_limits<double>::infinity()};
        while(!stack.empty()) {
            std::pair<Position, double> cItem = stack.top();
            stack.pop();
            
            Position currentNode{cItem.first};
            double g_n{cItem.second};
            double f_score{this->eval(currentNode, g_n)};
            path.push_back(currentNode);


            if(f_score > threshold) {
                // Threshold pruning
                //   - Skip this because its too expensive to search for
                //     in the current context
                // However, we now only consder paths that are too expensive
                min_f_score = std::min(f_score, min_f_score);
                continue;
            }

            if(this->isGoal(currentNode.first, currentNode.second)) {
                logger.Info("Solved Maze! Inside IDAStar()\n");
                return;
            }

            // Expand the neighbors
            for(const Position& dir : directions) {
                int newX{currentNode.first + dir.first};
                int newY{currentNode.second + dir.second};
                Position newPos{newX, newY};

                if(this->IsInvalid(newX, newY)) {
                    continue;
                }

                if(std::find(this->path.begin(), this->path.end(), newPos) == this->path.end()) {
                    // not in path
                    stack.emplace(newPos, g_n + 1);
                }
            }
        }
        path.clear();
        if(min_f_score == std::numeric_limits<double>::infinity()) {
            logger.Warn("Impossible Maze! Inside IDAStar()! \n");
            path.clear();

            return;
        }
        threshold = min_f_score;
    }  
}


template class Matrix<int>;