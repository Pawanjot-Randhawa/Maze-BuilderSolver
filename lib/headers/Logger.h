#ifndef LOGGER_H
#define LOGGER_H
#include <fstream>

#define LOG_FILE "log.txt"
#define logger Logger::GetInstance()

#include <fstream>

class Logger 
{
private:	

	enum class LogLevel
	{
		LOG_INFO = 0,
		LOG_DEBUG = 1,
		LOG_WARN = 2,
		LOG_ERROR = 3,
	};

	std::ofstream m_fileWriter{};

	void Log(const char* message, const LogLevel& level);
	Logger();

public:

	static Logger& GetInstance();

	void Debug(const char* message);
	void Info(const char* message);
	void Error(const char* message);
	void Warn(const char* message);

};


#endif  