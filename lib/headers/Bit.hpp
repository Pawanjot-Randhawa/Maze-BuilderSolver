#ifndef BIT_HPP
#define BIT_HPP

struct Bit {
  unsigned int flag : 1;

  Bit(const bool& flag = false) {
    this->flag = flag ? 1 : 0;
  }

  Bit(bool& flag) {
    this->flag = flag ? 1 : 0;
  }

  bool operator!() const {
    return this->flag == 0;
  }

  explicit operator bool() const {
    return this->flag == 1;
  }

};

#endif