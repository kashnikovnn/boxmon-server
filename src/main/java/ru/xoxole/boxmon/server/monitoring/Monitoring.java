package ru.xoxole.boxmon.server.monitoring;

import lombok.AllArgsConstructor;
import lombok.Data;
import ru.xoxole.boxmon.server.bot.BoxMonTelegramBot;
import ru.xoxole.boxmon.server.service.EspDataService;

@AllArgsConstructor
public abstract class Monitoring {

    final EspDataService espDataService;

    final BoxMonTelegramBot boxMonTelegramBot;

    MonotoringStatus status;

    public abstract void doCheck();
}
