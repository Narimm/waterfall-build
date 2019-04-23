package net.md_5.bungee.api;

import java.util.UUID;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class GameProfile
{
    @Getter
    private final UUID uniqueId;
    @Getter
    private final String username;
    @Getter
    private final Property[] properties;

    @Data
    @AllArgsConstructor
    public static class Property
    {
        private String name;
        private String value;
        private String signature;
    }
}
