package com.github.fernthedev;

import org.slf4j.Logger;

public interface IGame {

    INewEntityRegistry getEntityRegistry();

    Logger getLoggerImpl();
}
