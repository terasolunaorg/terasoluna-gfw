package org.terasoluna.gfw.common.message;

import static org.terasoluna.gfw.common.message.StandardResultMessageType.DANGER;
import static org.terasoluna.gfw.common.message.StandardResultMessageType.ERROR;
import static org.terasoluna.gfw.common.message.StandardResultMessageType.INFO;
import static org.terasoluna.gfw.common.message.StandardResultMessageType.SUCCESS;
import static org.terasoluna.gfw.common.message.StandardResultMessageType.WARN;
import static org.terasoluna.gfw.common.message.StandardResultMessageType.WARNING;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.Iterator;
import java.util.Set;
import java.util.function.Consumer;

import org.springframework.util.Assert;
import org.springframework.util.StringUtils;

/**
 * Types Messages which have {@link ResultMessageType} and map of {@link ResultMessages}
 */
public class TypesResultMessages implements Serializable, Iterable<ResultMessage>, Map<ResultMessageType, ResultMessages> {
    /**
     * serial version UID.
     */
    private static final long serialVersionUID = 1397838425857595762L;

    /**
     * main type
     */
    private final ResultMessageType mainType;

    /**
     * messages map
     */
    private final Map<ResultMessageType, ResultMessages> map = new HashMap<ResultMessageType, ResultMessages>();

    /**
     * default type
     */
    private static final ResultMessageType DEFAULT_TYPE = INFO;

    /**
     * default attribute name for TypesResultMessages
     */
    public static final String DEFAULT_TYPES_MESSAGES_ATTRIBUTE_NAME = StringUtils
            .uncapitalize(TypesResultMessages.class.getSimpleName());

    /**
     * Constructor.
     * @param typesMessages types messages to add
     */
    public TypesResultMessages(ResultMessages... typesMessages) {
        this(null, typesMessages);
    }

    /**
     * Constructor.
     * @param mainType main type
     * @param typesMessages types messages to add
     */
    public TypesResultMessages(ResultMessageType mainType, ResultMessages... typesMessages) {
        this.mainType = Objects.isNull(mainType) ? DEFAULT_TYPE : mainType;
        addAll(typesMessages);
    }

    /**
     * returns main type
     * @return main type
     */
    public ResultMessageType getMainType() {
        return mainType;
    }

    /**
     * returns map
     * @return map
     */
    public Map<ResultMessageType, ResultMessages> getMap() {
        return map;
    }

    /**
     * Outputs main type in this {@code TypesResultMessages} and the map of messages itself
     * @see java.lang.Object#toString()
     */
    @Override
    public String toString() {
        return "ResultMessages [main type=" + mainType + ", map=" + map + "]";
    }

    /**
     * add code and args to create by main type and add map
     * @param code message code
     * @param args replacement values of message format
     * @return this result types messages
     */
    public TypesResultMessages add(String code, Object... args) {
        return add(mainType, code, args);
    }

    /**
     * add code and args to create by type and add map
     * @param type message type
     * @param code message code
     * @param args replacement values of message format
     * @return this result types messages
     */
    public TypesResultMessages add(ResultMessageType type, String code, Object... args) {
        push(new ResultMessages(type).add(code, args));
        return this;
    }

    /**
     * add a ResultMessages
     * @param resultMessages ResultMessages instance
     * @return this result types messages
     */
    public TypesResultMessages add(ResultMessages resultMessages) {
        if (Objects.nonNull(resultMessages)) {
            push(resultMessages);
        } else {
            throw new IllegalArgumentException("resultMessages must not be null");
        }
        return this;
    }

    /**
     * add all types resultMessages (excludes <code>null</code> types resultMessages)<br>
     * <p>
     * if <code>types resultMessages</code> is <code>null</code>, no type resultMessages is added.
     * </p>
     * @param typesMessages types resultMessages to add
     * @return this result types messages
     */
    public TypesResultMessages addAll(ResultMessages... typesMessages) {
        Assert.notNull(typesMessages, "types resultMessages must not be null");
        pushAll(typesMessages);
        return this;
    }

    /**
     * add all types resultMessages (excludes <code>null</code> types resultMessages)<br>
     * <p>
     * if <code>types resultMessages</code> is <code>null</code>, no types resultMessages is added.
     * </p>
     * @param typesMessages types messages to add
     * @return this result types messages
     */
    public TypesResultMessages addAll(Collection<ResultMessages> typesMessages) {
        Assert.notNull(typesMessages, "types resultMessages must not be null");
        typesMessages.forEach(resultMessages -> add(resultMessages));
        return this;
    }

    /**
     * Returns {@link Iterator} instance that iterates over main type list of {@link ResultMessage}
     * @see java.lang.Iterable#iterator()
     */
    @Override
    public Iterator<ResultMessage> iterator() {
        return iterator(mainType);
    }

    /**
     * Returns {@link Iterator} instance that iterates over a type list of {@link ResultMessage}
     * @see java.lang.Iterable#iterator()
     * @param type message type
     * @return iterator instance
     */
    public Iterator<ResultMessage> iterator(ResultMessageType type) {
        return initTypeNotExist(type).iterator();
    }

    /**
     * select a type to operate.
     * @param type message type
     * @param action The action to be performed for each element
     * @throws NullPointerException if the specified action is null
     */
    public void forEach(ResultMessageType type, Consumer<ResultMessage> action) {
        Objects.requireNonNull(action);
        Iterator<ResultMessage> iterator = iterator(type);
        while (iterator.hasNext()) {
            action.accept(iterator.next());
        }
    }

    /**
     * factory method for success messages.
     * @return this result types messages's main type is success
     */
    public static TypesResultMessages success() {
        return new TypesResultMessages(SUCCESS);
    }

    /**
     * factory method for info messages.
     * @return this result types messages's main type is info
     */
    public static TypesResultMessages info() {
        return new TypesResultMessages(DEFAULT_TYPE);
    }

    /**
     * factory method for warn messages.
     * @return this result types messages's main type is warn
     * @deprecated Instead of this method, please use {@link #warning()}. This method will be removed in the future.
     */
    @Deprecated
    public static TypesResultMessages warn() {
        return new TypesResultMessages(WARN);
    }

    /**
     * factory method for warning messages.
     * @return this result types messages's main type is warning
     * @since 5.0.0
     */
    public static TypesResultMessages warning() {
        return new TypesResultMessages(WARNING);
    }

    /**
     * factory method for error messages.
     * @return this result types messages's main type is error
     */
    public static TypesResultMessages error() {
        return new TypesResultMessages(ERROR);
    }

    /**
     * factory method for danger messages.
     * @return this result types messages's main type is danger
     */
    public static TypesResultMessages danger() {
        return new TypesResultMessages(DANGER);
    }

    /**
     * Returns the number of types this map.  If the
     * map contains more than <tt>Integer.MAX_VALUE</tt> elements, returns
     * <tt>Integer.MAX_VALUE</tt>.
     *
     * @return the number of types in this map
     */
    @Override
    public int size() {
        return map.size();
    }

    /**
     * Returns <tt>true</tt> if this map contains types.
     *
     * @return <tt>true</tt> if this map contains no types
     */
    public boolean isNotEmpty() {
        return !isEmpty();
    }

    /**
     * Returns <tt>true</tt> if this map contains no types.
     *
     * @return <tt>true</tt> if this map contains no types
     */
    @Override
    public boolean isEmpty() {
        return map.isEmpty();
    }

    /**
     * Returns <tt>true</tt> if this map contains a mapping for type.
     *
     * @param   type   The type whose presence in this map is to be tested
     * @return <tt>true</tt> if this map contains a mapping for the type.
     */
    @Override
    public boolean containsKey(Object type) {
        return map.containsKey(type);
    }

    /**
     * Returns <tt>true</tt> if this map contains a mapping for the
     * specified value.
     *
     * @param   resultMessages   The value whose presence in this map is to be tested
     * @return <tt>true</tt> if this map contains a mapping for the specified
     * value.
     */
    @Override
    public boolean containsValue(Object resultMessages) {
        return map.containsValue(resultMessages);
    }

    /**
     * Returns <tt>value</tt> if this map contains a mapping for the type.
     *
     * @param   type   The type whose presence in this map is to be tested
     * @return <tt>value</tt> if this map contains a mapping for the type.
     */
    @Override
    public ResultMessages get(Object type) {
        return map.get(type);
    }

    /**
     * Associates the resultMessages with the type in this map
     * (optional operation).  If the map previously contained a mapping for
     * the type, the old resultMessages is replaced by the resultMessages.
     * @param type message type
     * @param resultMessages resultMessages instance
     * @return old resultMessages instance
     */
    @Override
    public ResultMessages put(ResultMessageType type, ResultMessages resultMessages) {
        Assert.notNull(type, "type must not be null");
        Assert.notNull(resultMessages, "resultMessages must not be null");
        return map.put(type, resultMessages);
    }

    /**
     * Copies all of the mappings from the specified map to this map
     * (optional operation).
     * @param map specified map
     */
    @Override
    public void putAll(Map<? extends ResultMessageType, ? extends ResultMessages> map) {
        map.forEach((k, v) -> put(k, v));
    }

    /**
     * Removes the mapping for a type from this map if it is present
     * @param type message type
     * @return <tt>value</tt> if this map contains a mapping for the type.
     */
    @Override
    public ResultMessages remove(Object type) {
        return map.remove(type);
    }

    /**
     * Removes all of the mappings from this map (optional operation).
     * The map will be empty after this call returns.
     */
    @Override
    public void clear() {
        map.clear();
    }

    /**
     * Returns a {@link Set} view of the keys contained in this map.
     * @return a set view of the keys
     */
    @Override
    public Set<ResultMessageType> keySet() {
        return map.keySet();
    }

    /**
     * Returns a {@link Collection} view of the values contained in this map.
     * @return a collection view of the values
     */
    @Override
    public Collection<ResultMessages> values() {
        return map.values();
    }

    /**
     * Returns a {@link Set} view of the mappings contained in this map.
     * @return a set view of the mappings
     */
    @Override
    public Set<Entry<ResultMessageType, ResultMessages>> entrySet() {
        return map.entrySet();
    }

    /**
     * the same main type resultMessages can be add.
     * @param typesMessages types messages
     * @return this result types messages
     */
    public TypesResultMessages mainTypeGroup(ResultMessages... typesMessages) {
        return group(mainType, typesMessages);
    }

    /**
     * the same type resultMessages can be add, otherwise throw exception
     * @param type message type
     * @param typesMessages types messages
     * @return this result types messages
     */
    public TypesResultMessages group(ResultMessageType type, ResultMessages... typesMessages) {
        Assert.notNull(type, "type must not be null");
        if (Objects.nonNull(typesMessages)) {
            for (ResultMessages resultMessages : typesMessages) {
                if (Objects.nonNull(resultMessages)) {
                    if (typeIsEquals(resultMessages.getType(), type)) {
                        push(resultMessages);
                    } else {
                        throw new IllegalArgumentException("resultMessages type must not be " + type);
                    }
                } else {
                    throw new IllegalArgumentException("resultMessages must not be null");
                }
            }
        } else {
            throw new IllegalArgumentException("types Messages must not be null");
        }
        return this;
    }

    /**
     * returns whether main type messages are not empty.
     * @return whether main type messages are not empty
     */
    public boolean mainTypeIsNotEmpty() {
        return isNotEmptyByType(mainType);
    }

    /**
     * returns whether type messages are not empty.
     * @param type message type
     * @return whether type messages are not empty
     */
    public boolean isNotEmptyByType(ResultMessageType type) {
        ResultMessages resultMessages = get(type);
        if (Objects.isNull(resultMessages)) {
            return false;
        }
        return resultMessages.isNotEmpty();
    }

    /**
     * Returns {@code true} if the arguments are equal to each other
     * and {@code false} otherwise.
     * @param compareType compare type
     * @param comparedType compared type
     * @return {@code true} if the arguments are equal to each other
     */
    private boolean typeIsEquals(ResultMessageType compareType, ResultMessageType comparedType) {
        return Objects.equals(compareType, comparedType);
    }

    /**
     * push messages(batch).
     * invoke this method's resultMessages instance must not be null.
     * @param typesMessages types resultMessages instances
     */
    private void pushAll(ResultMessages... typesMessages) {
        for (ResultMessages resultMessages : typesMessages) {
            add(resultMessages);
        }
    }

    /**
     * push messages.
     * invoke this method's resultMessages instance must not be null.
     * @param resultMessages resultMessages instance
     */
    private void push(ResultMessages resultMessages) {
        initTypeNotExist(resultMessages.getType()).addAll(resultMessages.getList());
    }

    /**
     *  get or create a ResultMessages instance.
     * <p>
     * if <code>resultMessages</code> is <code>null</code>, create new ResultMessages instance is added.
     * </p>
     * <p>
     * if <code>resultMessages</code> is <code>instance</code>, return.
     * </p>
     * @param type message type
     * @return ResultMessages instance
     */
    private ResultMessages initTypeNotExist(ResultMessageType type) {
        ResultMessages resultMessages = get(type);
        if (Objects.isNull(resultMessages)) {
            resultMessages = new ResultMessages(type);
            map.put(type, resultMessages);
        }
        return resultMessages;
    }

    /**
     * special handling for the serialization and deserialization process
     * @param out ObjectOutputStream
     * @throws IOException {@link java.io.ObjectOutputStream#defaultWriteObject()}
     * @see java.io.Serializable
     */
    private void writeObject(ObjectOutputStream out) throws IOException {
        out.defaultWriteObject();
    }

    /**
     * special handling for the serialization and deserialization process
     * @param in ObjectInputStream
     * @throws IOException {@link java.io.ObjectInputStream#defaultReadObject()}
     * @throws ClassNotFoundException {@link java.io.ObjectInputStream#defaultReadObject()}
     * @see java.io.Serializable
     */
    private void readObject(
            ObjectInputStream in) throws IOException, ClassNotFoundException {
        in.defaultReadObject();
    }
}
