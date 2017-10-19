/*
 * Copyright (C) 2013-2017 NTT DATA Corporation
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND,
 * either express or implied. See the License for the specific language
 * governing permissions and limitations under the License.
 */
package org.terasoluna.gfw.common.codelist;

import java.util.Collection;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

/**
 * {@link Map} implementation class which enables thread-safe operations on the map of key-value pairs contained in it.
 * @param <K> Key
 * @param <V> Value
 */
public class ReadWriteLockMapWrapper<K, V> implements Map<K, V> {
    /**
     * backing map to be locked
     */
    private final Map<K, V> map;

    /**
     * Instance of {@link ReadWriteLock} implementation
     */
    private final ReadWriteLock lock;

    /**
     * Constructor with a single {@link Map} parameter.
     * @param map target map of read or write lock
     */
    public ReadWriteLockMapWrapper(Map<K, V> map) {
        this.map = map;
        this.lock = new ReentrantReadWriteLock();
    }

    /**
     * A contract to represent a method call of enclosing {@link Map} instance.
     * <p>
     * Implementation of this interface must first acquire read or write lock using implementation of <br>
     * {@link ReadWriteLock} before calling the method of enclosing {@link Map}. <br>
     * </p>
     * @param <T> The return type of the method represented by the instance of implementation of this interface.
     */
    public interface LockedCallback<T> {

        /**
         * Implementation must first acquire read or write lock before calling the method represented by this contract <br>
         * @return value returned by the method call
         */
        T apply();
    }

    /**
     * Provides read locked call to a method of {@link Map}. {@link Map} is not locked exclusively. <br>
     * <p>
     * {@link Map} is the shared resource which is wrapped in this class. Access to this shared resource is regulated using
     * {@link ReadWriteLock} implementation. A read lock can be acquired by as any number of threads. A read lock does not block
     * other read locks.
     * </p>
     * <p>
     * A {@link LockedCallback} instance passed as argument to this method. It represents a method call of the {@link Map}.<br>
     * A read lock is first acquired over the {@code Map} and then using {@code callback}, method represented by
     * {@code callback} <br>
     * is executed.
     * </p>
     * @param <T> a return value type of callback method within read lock
     * @param callback An instance of {@link LockedCallback} which represents a method call of {@link Map}
     * @return the return value of the method represented by {@code callback}
     */
    public <T> T withReadLock(LockedCallback<T> callback) {
        Lock readLock = this.lock.readLock();
        T result = null;
        try {
            readLock.lock();
            result = callback.apply();
        } finally {
            readLock.unlock();
        }
        return result;
    }

    /**
     * Provides write locked call to a method of {@link Map}. {@link Map} is exclusively locked for write operation. <br>
     * <p>
     * {@link Map} is the shared resource which is wrapped in this class. Access to this shared resource is regulated <br>
     * using {@link ReadWriteLock} implementation. A write lock can be acquired by only a single thread. No read lock can be <br>
     * acquired while the resource is write locked. A write lock blocks other write locks as well as read locks. <br>
     * </p>
     * <p>
     * A {@link LockedCallback} instance passed as argument to this method. It represents a method call of the {@link Map}.<br>
     * A read lock is first acquired over the {@code Map} and then using {@code callback}, method represented by
     * {@code callback} <br>
     * is executed.<br>
     * </p>
     * @param <T> a return value type of callback method within write lock
     * @param callback An instance of {@link LockedCallback} which represents a method call of {@link Map}
     * @return the return value of the method represented by {@code callback}
     */
    public <T> T withWriteLock(LockedCallback<T> callback) {
        Lock writeLock = this.lock.writeLock();
        T result = null;
        try {
            writeLock.lock();
            result = callback.apply();
        } finally {
            writeLock.unlock();
        }
        return result;
    }

    /**
     * A read locked call to {@code size()} method of {@link Map}
     * @see java.util.Map#size()
     */
    @Override
    public int size() {
        return withReadLock(new LockedCallback<Integer>() {
            @Override
            public Integer apply() {
                return map.size();
            }
        });
    }

    /**
     * A read locked call to {@code isEmpty()} method of {@link Map}
     * @see java.util.Map#isEmpty()
     */
    @Override
    public boolean isEmpty() {
        return withReadLock(new LockedCallback<Boolean>() {
            @Override
            public Boolean apply() {
                return map.isEmpty();
            }
        });
    }

    /**
     * A read locked call to {@code containsKey()} method of {@link Map}
     * @see java.util.Map#containsKey(java.lang.Object)
     */
    @Override
    public boolean containsKey(final Object key) {
        return withReadLock(new LockedCallback<Boolean>() {
            @Override
            public Boolean apply() {
                return map.containsKey(key);
            }
        });
    }

    /**
     * A read locked call to {@code containsValue()} method of {@link Map}
     * @see java.util.Map#containsValue(java.lang.Object)
     */
    @Override
    public boolean containsValue(final Object value) {
        return withReadLock(new LockedCallback<Boolean>() {
            @Override
            public Boolean apply() {
                return map.containsValue(value);
            }
        });
    }

    /**
     * A read locked call to {@code get()} method of {@link Map}
     * @see java.util.Map#get(java.lang.Object)
     */
    @Override
    public V get(final Object key) {
        return withReadLock(new LockedCallback<V>() {
            @Override
            public V apply() {
                return map.get(key);
            }
        });
    }

    /**
     * A write locked call to {@code put()} method of {@link Map}
     * @see java.util.Map#put(java.lang.Object, java.lang.Object)
     */
    @Override
    public V put(final K key, final V value) {
        return withWriteLock(new LockedCallback<V>() {
            @Override
            public V apply() {
                return map.put(key, value);
            }
        });
    }

    /**
     * A write locked call to {@code remove()} method of {@link Map}
     * @see java.util.Map#remove(java.lang.Object)
     */
    @Override
    public V remove(final Object key) {
        return withWriteLock(new LockedCallback<V>() {
            @Override
            public V apply() {
                return map.remove(key);
            }
        });
    }

    /**
     * A write locked call to {@code putAll()} method of {@link Map}
     * @see java.util.Map#putAll(java.util.Map)
     */
    @Override
    public void putAll(final Map<? extends K, ? extends V> m) {
        withWriteLock(new LockedCallback<Void>() {
            @Override
            public Void apply() {
                map.putAll(m);
                return null;
            }
        });
    }

    /**
     * A write locked call to {@code clear()} method of {@link Map}
     * @see java.util.Map#clear()
     */
    @Override
    public void clear() {
        withWriteLock(new LockedCallback<Void>() {
            @Override
            public Void apply() {
                map.clear();
                return null;
            }
        });
    }

    /**
     * A write locked call to {@code clear()} and {@link #putAll(java.util.Map)} methods of {@link Map}
     * <p>
     * Clears the {@link Map} which is encapsulated in this class and loads it with new values of<br>
     * the {@link Map} received as argument.
     * @param m {@link Map} with new values
     */
    public void clearAndPutAll(final Map<? extends K, ? extends V> m) {
        withWriteLock(new LockedCallback<Void>() {
            @Override
            public Void apply() {
                map.clear();
                map.putAll(m);
                return null;
            }
        });
    }

    /**
     * A read locked call to {@code keySet()} method of {@link Map}
     * @see java.util.Map#keySet()
     */
    @Override
    public Set<K> keySet() {
        return withReadLock(new LockedCallback<Set<K>>() {
            @Override
            public Set<K> apply() {
                return map.keySet();
            }
        });
    }

    /**
     * A read locked call to {@code values()} method of {@link Map}
     * @see java.util.Map#values()
     */
    @Override
    public Collection<V> values() {
        return withReadLock(new LockedCallback<Collection<V>>() {
            @Override
            public Collection<V> apply() {
                return map.values();
            }
        });
    }

    /**
     * A read locked call to {@code entrySet()} method of {@link Map}
     * @see java.util.Map#entrySet()
     */
    @Override
    public Set<java.util.Map.Entry<K, V>> entrySet() {
        return withReadLock(
                new LockedCallback<Set<java.util.Map.Entry<K, V>>>() {
                    @Override
                    public Set<java.util.Map.Entry<K, V>> apply() {
                        return map.entrySet();
                    }
                });
    }

    /**
     * A read locked call to {@code equals()} method of {@link Map}
     * @see java.lang.Object#equals(java.lang.Object)
     */
    @Override
    public boolean equals(final Object o) {
        return withReadLock(new LockedCallback<Boolean>() {
            @Override
            public Boolean apply() {
                return map.equals(o);
            }
        });
    }

    /**
     * A read locked call to {@code hashCode()} method of {@link Map}
     * @see java.lang.Object#hashCode()
     */
    @Override
    public int hashCode() {
        return withReadLock(new LockedCallback<Integer>() {
            @Override
            public Integer apply() {
                return map.hashCode();
            }
        });
    }
}
