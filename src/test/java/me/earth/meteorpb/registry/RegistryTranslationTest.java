package me.earth.meteorpb.registry;

import me.earth.pingbypass.api.registry.Registry;
import me.earth.pingbypass.api.registry.impl.OrderedRegistryImpl;
import me.earth.pingbypass.api.traits.Nameable;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class RegistryTranslationTest {
    @Test
    public void testRegistryTranslation() {
        Registry<Nameable> registry = new OrderedRegistryImpl<>();
        registry.register(() -> "test");

        var mcRegistry = RegistryTranslation.fromPingBypassRegistry("testregistry", registry);
        boolean[] found = new boolean[]{false};
        mcRegistry.forEach(n -> {
            found[0] = true;
            assertEquals("test", n.getName());
        });

        assertTrue(found[0]);
    }

}
