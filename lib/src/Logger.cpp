#include "../headers/Logger.h"

Logger::Logger()
{
	if (!this->m_fileWriter.is_open())
		this->m_fileWriter.open(LOG_FILE, std::ios::trunc);
	
	if (!m_fileWriter.is_open())
		throw std::runtime_error("Failed to open log file");
}

Logger& Logger::GetInstance()
{
	static Logger instance;
	return instance;
}


void Logger::Debug(const char* message)
{
	this->Log(message, LogLevel::LOG_DEBUG);
}


void Logger::Info(const char* message)
{
	this->Log(message, LogLevel::LOG_INFO);
}


void Logger::Error(const char* message)
{
	this->Log(message, LogLevel::LOG_ERROR);
}


void Logger::Warn(const char* message)
{
	this->Log(message, LogLevel::LOG_WARN);
}


void Logger::Log(const char* message, const LogLevel& level)
{
	switch (level) 
	{
	case LogLevel::LOG_DEBUG:
		this->m_fileWriter << "[DEBUG]: ";
		break;

	case LogLevel::LOG_ERROR:
		this->m_fileWriter << "[ERROR]: ";
		break;

	case LogLevel::LOG_WARN:
		this->m_fileWriter << "[WARN]: ";
		break;

	default:
		this->m_fileWriter << "[INFO]: ";
	}

	this->m_fileWriter << message << "\n\n";
	this->m_fileWriter.flush();
}