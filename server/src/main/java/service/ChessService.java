package service;

import model.*;
import dataAccess.DataAccess;

public class ChessService {
    private final DataAccess dataAccess;

    public ChessService() {
        dataAccess = new DataAccess();
    }

}
