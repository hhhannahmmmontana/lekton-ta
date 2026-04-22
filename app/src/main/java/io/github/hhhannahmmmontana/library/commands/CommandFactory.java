package io.github.hhhannahmmmontana.library.commands;

import io.github.hhhannahmmmontana.library.exceptions.InvalidCommandFormatException;
import lombok.NoArgsConstructor;

/**
 * Фабрика для создания объектов команд на основе строкового ввода.
 * <p>
 * Класс анализирует строковую команду, определяет её тип и делегирует
 * создание конкретного экземпляра {@link Command} соответствующему
 * статическому фабричному методу.
 */
@NoArgsConstructor
public final class CommandFactory {
    /**
     * Создает команду на основе переданной строки.
     *
     * @param strCommand полная строка команды с аргументами
     * @return конкретная реализация {@link Command}, соответствующая введенному имени
     * @throws InvalidCommandFormatException если имя команды неизвестно или
     *                                       формат аргументов некорректен
     */
    public Command createCommand(
        String strCommand
    ) throws InvalidCommandFormatException {
        var commandName = strCommand.split(" ")[0];
        var removalLength = commandName.length() + 1;
        var arg = (removalLength < strCommand.length())
            ? strCommand.substring(removalLength)
            : null;
        return switch (commandName) {
            case "LIST" -> ListCommand.fromStringArg(arg);
            case "ADD" -> AddCommand.fromStringArg(arg);
            case "REMOVE" -> RemoveCommand.fromStringArg(arg);
            case "FIND" -> FindCommand.fromStringArg(arg);
            case "STATS" -> StatsCommand.fromStringArg(arg);
            default -> throw new InvalidCommandFormatException();
        };
    }
}
