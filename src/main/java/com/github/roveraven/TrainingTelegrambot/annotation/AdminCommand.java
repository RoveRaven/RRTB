package com.github.roveraven.TrainingTelegrambot.annotation;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import com.github.roveraven.TrainingTelegrambot.command.Command;
/**
 * Mark if {@link Command} can be viewed only by admins.
 */
@Retention(RetentionPolicy.RUNTIME)
public @interface AdminCommand {
}
