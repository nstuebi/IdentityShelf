# Error Handling Configuration

This project now uses a configuration-based error handling system that determines the level of detail shown in error responses.

## Configuration

Set the `app.error-handling.detail-level` property in `application.properties`:

```properties
# Options: BASIC, ENHANCED, FULL
app.error-handling.detail-level=FULL
```

## Error Detail Levels

### BASIC
- Only shows basic error message
- Minimal information for production use
- Example response:
```json
{
  "timestamp": "2024-01-01T12:00:00",
  "status": 500,
  "error": "Internal Server Error",
  "path": "/api/test/error/basic",
  "message": "This is a basic error message"
}
```

### ENHANCED
- Shows error message + exception class + root cause
- Good for development without exposing stack traces
- Example response:
```json
{
  "timestamp": "2024-01-01T12:00:00",
  "status": 500,
  "error": "Internal Server Error",
  "path": "/api/test/error/enhanced",
  "message": "Invalid parameter provided",
  "exception": "IllegalArgumentException"
}
```

### FULL
- Shows complete error information including stack traces
- Best for development and debugging
- Example response:
```json
{
  "timestamp": "2024-01-01T12:00:00",
  "status": 500,
  "error": "Internal Server Error",
  "path": "/api/test/error/full",
  "message": "This error should show full details including stack trace",
  "exception": "java.lang.RuntimeException",
  "stackTrace": "org.identityshelf.config.TestErrorController.triggerFullError(TestErrorController.java:25)\n..."
}
```

## Testing

Use the test endpoints to see different error detail levels:

- `/api/test/error/basic` - Basic error
- `/api/test/error/enhanced` - Enhanced error
- `/api/test/error/full` - Full error with stack trace
- `/api/test/error/nested` - Error with root cause

## Frontend Integration

The frontend no longer needs to know about developer mode. It simply displays the error information it receives from the backend. Error details are always shown when available, regardless of configuration.

## Benefits

1. **Centralized Control**: Error detail level is controlled entirely by backend configuration
2. **Environment Flexibility**: Easy to switch between detail levels for different environments
3. **Security**: Production can use BASIC level to avoid exposing sensitive information
4. **Developer Experience**: FULL level provides comprehensive debugging information
5. **Clean Architecture**: Frontend doesn't need to know about error handling configuration

