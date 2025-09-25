package edu.ccrm.service;

import java.io.IOException;

public interface Persistable {
    void exportToFile(String filename) throws IOException;
    void importFromFile(String filename) throws IOException;
}