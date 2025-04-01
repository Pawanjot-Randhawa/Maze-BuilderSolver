#ifndef BIT_HPP
#define BIT_HPP

struct Bit {
  unsigned int flag : 1;

  Bit(const bool& flag = false) {
    this->flag = flag ? 1 : 0;
  }
};

#endif