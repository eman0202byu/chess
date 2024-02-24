package service;

public record ServiceReport(ChessService.StatusCodes Status, String ErrorLogging) {
}
