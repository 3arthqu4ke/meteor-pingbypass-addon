package me.earth.meteorpb.registry;

import com.mojang.datafixers.util.Pair;
import com.mojang.serialization.Lifecycle;
import me.earth.pingbypass.api.traits.Nameable;
import net.minecraft.registry.Registry;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.SimpleRegistry;
import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.registry.entry.RegistryEntryList;
import net.minecraft.registry.tag.TagKey;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.random.Random;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.*;
import java.util.stream.Stream;

public class RegistryTranslation {
    public static <T extends Nameable> Registry<T> fromPingBypassRegistry(String name, me.earth.pingbypass.api.registry.Registry<T> registry) {
        return new SimpleRegistry<>(RegistryKey.ofRegistry(new Identifier("pingbypass", name)), Lifecycle.stable(), true) {
            @Override
            public int size() {
                return registry.size();
            }

            @Override
            public Identifier getId(T entry) {
                return null;
            }

            @Override
            public Optional<RegistryKey<T>> getKey(T entry) {
                return Optional.empty();
            }

            @Override
            public int getRawId(T entry) {
                return 0;
            }

            @Override
            public T get(RegistryKey<T> key) {
                return null;
            }

            @Override
            public T get(Identifier id) {
                return null;
            }

            @Override
            public Lifecycle getEntryLifecycle(T object) {
                return null;
            }

            @Override
            public Lifecycle getLifecycle() {
                return null;
            }

            @Override
            public Set<Identifier> getIds() {
                return null;
            }

            @Override
            public boolean containsId(Identifier id) {
                return false;
            }

            @Nullable
            @Override
            public T get(int index) {
                return null;
            }

            @Override
            public @NotNull Iterator<T> iterator() {
                return registry.iterator();
            }

            @Override
            public boolean contains(RegistryKey<T> key) {
                return false;
            }

            @Override
            public Set<Map.Entry<RegistryKey<T>, T>> getEntrySet() {
                return null;
            }

            @Override
            public Set<RegistryKey<T>> getKeys() {
                return null;
            }

            @Override
            public Optional<RegistryEntry.Reference<T>> getRandom(Random random) {
                return Optional.empty();
            }

            @Override
            public Registry<T> freeze() {
                return null;
            }

            @Override
            public RegistryEntry.Reference<T> createEntry(T value) {
                return null;
            }

            @Override
            public Optional<RegistryEntry.Reference<T>> getEntry(int rawId) {
                return Optional.empty();
            }

            @Override
            public Optional<RegistryEntry.Reference<T>> getEntry(RegistryKey<T> key) {
                return Optional.empty();
            }

            @Override
            public Stream<RegistryEntry.Reference<T>> streamEntries() {
                return null;
            }

            @Override
            public Optional<RegistryEntryList.Named<T>> getEntryList(TagKey<T> tag) {
                return Optional.empty();
            }

            @Override
            public RegistryEntryList.Named<T> getOrCreateEntryList(TagKey<T> tag) {
                return null;
            }

            @Override
            public Stream<Pair<TagKey<T>, RegistryEntryList.Named<T>>> streamTagsAndEntries() {
                return null;
            }

            @Override
            public Stream<TagKey<T>> streamTags() {
                return null;
            }

            @Override
            public void clearTags() {
            }

            @Override
            public void populateTags(Map<TagKey<T>, List<RegistryEntry<T>>> tagEntries) {
            }
        };
    }

}
