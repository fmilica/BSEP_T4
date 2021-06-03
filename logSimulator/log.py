class Log:
    def __init__(self, timestamp, level, message, source, type, ipAddress, error, statusCode):
        self.timestamp = timestamp
        self.level = level
        self.message = message
        self.source = source
        self.type = type
        self.ipAddress = ipAddress
        self.error = error
        self.statusCode = statusCode