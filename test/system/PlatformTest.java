package eu.menzani.ringbuffer.system;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class PlatformTest {
    @Test
    public void isWindows() {
        assertTrue(Platform.WINDOWS_32.isWindows());
        assertTrue(Platform.WINDOWS_64.isWindows());
    }

    @Test
    public void isLinux() {
        assertTrue(Platform.LINUX_32.isLinux());
        assertTrue(Platform.LINUX_64.isLinux());
    }

    @Test
    public void is32Bit() {
        assertTrue(Platform.WINDOWS_32.is32Bit());
        assertTrue(Platform.LINUX_32.is32Bit());
    }

    @Test
    public void is64Bit() {
        assertTrue(Platform.WINDOWS_64.is64Bit());
        assertTrue(Platform.LINUX_64.is64Bit());
    }
}