/**
 * Autogenerated by Thrift Compiler (0.9.1)
 *
 * DO NOT EDIT UNLESS YOU ARE SURE THAT YOU KNOW WHAT YOU ARE DOING
 *  @generated
 */
package com.tingleff.yassg.search.types;

import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.thrift.scheme.IScheme;
import org.apache.thrift.scheme.SchemeFactory;
import org.apache.thrift.scheme.StandardScheme;

import org.apache.thrift.scheme.TupleScheme;
import org.apache.thrift.protocol.TTupleProtocol;
import org.apache.thrift.protocol.TProtocolException;
import org.apache.thrift.EncodingUtils;
import org.apache.thrift.TException;
import org.apache.thrift.async.AsyncMethodCallback;
import org.apache.thrift.server.AbstractNonblockingServer.*;
import java.util.List;
import java.util.ArrayList;
import java.util.Map;
import java.util.HashMap;
import java.util.EnumMap;
import java.util.Set;
import java.util.HashSet;
import java.util.EnumSet;
import java.util.Collections;
import java.util.BitSet;
import java.nio.ByteBuffer;
import java.util.Arrays;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class TDevice implements org.apache.thrift.TBase<TDevice, TDevice._Fields>, java.io.Serializable, Cloneable, Comparable<TDevice> {
  private static final org.apache.thrift.protocol.TStruct STRUCT_DESC = new org.apache.thrift.protocol.TStruct("TDevice");

  private static final org.apache.thrift.protocol.TField IP_FIELD_DESC = new org.apache.thrift.protocol.TField("ip", org.apache.thrift.protocol.TType.STRING, (short)1);
  private static final org.apache.thrift.protocol.TField UA_FIELD_DESC = new org.apache.thrift.protocol.TField("ua", org.apache.thrift.protocol.TType.STRING, (short)2);

  private static final Map<Class<? extends IScheme>, SchemeFactory> schemes = new HashMap<Class<? extends IScheme>, SchemeFactory>();
  static {
    schemes.put(StandardScheme.class, new TDeviceStandardSchemeFactory());
    schemes.put(TupleScheme.class, new TDeviceTupleSchemeFactory());
  }

  private String ip; // optional
  private String ua; // optional

  /** The set of fields this struct contains, along with convenience methods for finding and manipulating them. */
  public enum _Fields implements org.apache.thrift.TFieldIdEnum {
    IP((short)1, "ip"),
    UA((short)2, "ua");

    private static final Map<String, _Fields> byName = new HashMap<String, _Fields>();

    static {
      for (_Fields field : EnumSet.allOf(_Fields.class)) {
        byName.put(field.getFieldName(), field);
      }
    }

    /**
     * Find the _Fields constant that matches fieldId, or null if its not found.
     */
    public static _Fields findByThriftId(int fieldId) {
      switch(fieldId) {
        case 1: // IP
          return IP;
        case 2: // UA
          return UA;
        default:
          return null;
      }
    }

    /**
     * Find the _Fields constant that matches fieldId, throwing an exception
     * if it is not found.
     */
    public static _Fields findByThriftIdOrThrow(int fieldId) {
      _Fields fields = findByThriftId(fieldId);
      if (fields == null) throw new IllegalArgumentException("Field " + fieldId + " doesn't exist!");
      return fields;
    }

    /**
     * Find the _Fields constant that matches name, or null if its not found.
     */
    public static _Fields findByName(String name) {
      return byName.get(name);
    }

    private final short _thriftId;
    private final String _fieldName;

    _Fields(short thriftId, String fieldName) {
      _thriftId = thriftId;
      _fieldName = fieldName;
    }

    public short getThriftFieldId() {
      return _thriftId;
    }

    public String getFieldName() {
      return _fieldName;
    }
  }

  // isset id assignments
  private _Fields optionals[] = {_Fields.IP,_Fields.UA};
  public static final Map<_Fields, org.apache.thrift.meta_data.FieldMetaData> metaDataMap;
  static {
    Map<_Fields, org.apache.thrift.meta_data.FieldMetaData> tmpMap = new EnumMap<_Fields, org.apache.thrift.meta_data.FieldMetaData>(_Fields.class);
    tmpMap.put(_Fields.IP, new org.apache.thrift.meta_data.FieldMetaData("ip", org.apache.thrift.TFieldRequirementType.OPTIONAL, 
        new org.apache.thrift.meta_data.FieldValueMetaData(org.apache.thrift.protocol.TType.STRING)));
    tmpMap.put(_Fields.UA, new org.apache.thrift.meta_data.FieldMetaData("ua", org.apache.thrift.TFieldRequirementType.OPTIONAL, 
        new org.apache.thrift.meta_data.FieldValueMetaData(org.apache.thrift.protocol.TType.STRING)));
    metaDataMap = Collections.unmodifiableMap(tmpMap);
    org.apache.thrift.meta_data.FieldMetaData.addStructMetaDataMap(TDevice.class, metaDataMap);
  }

  public TDevice() {
  }

  /**
   * Performs a deep copy on <i>other</i>.
   */
  public TDevice(TDevice other) {
    if (other.isSetIp()) {
      this.ip = other.ip;
    }
    if (other.isSetUa()) {
      this.ua = other.ua;
    }
  }

  public TDevice deepCopy() {
    return new TDevice(this);
  }

  @Override
  public void clear() {
    this.ip = null;
    this.ua = null;
  }

  public String getIp() {
    return this.ip;
  }

  public void setIp(String ip) {
    this.ip = ip;
  }

  public void unsetIp() {
    this.ip = null;
  }

  /** Returns true if field ip is set (has been assigned a value) and false otherwise */
  public boolean isSetIp() {
    return this.ip != null;
  }

  public void setIpIsSet(boolean value) {
    if (!value) {
      this.ip = null;
    }
  }

  public String getUa() {
    return this.ua;
  }

  public void setUa(String ua) {
    this.ua = ua;
  }

  public void unsetUa() {
    this.ua = null;
  }

  /** Returns true if field ua is set (has been assigned a value) and false otherwise */
  public boolean isSetUa() {
    return this.ua != null;
  }

  public void setUaIsSet(boolean value) {
    if (!value) {
      this.ua = null;
    }
  }

  public void setFieldValue(_Fields field, Object value) {
    switch (field) {
    case IP:
      if (value == null) {
        unsetIp();
      } else {
        setIp((String)value);
      }
      break;

    case UA:
      if (value == null) {
        unsetUa();
      } else {
        setUa((String)value);
      }
      break;

    }
  }

  public Object getFieldValue(_Fields field) {
    switch (field) {
    case IP:
      return getIp();

    case UA:
      return getUa();

    }
    throw new IllegalStateException();
  }

  /** Returns true if field corresponding to fieldID is set (has been assigned a value) and false otherwise */
  public boolean isSet(_Fields field) {
    if (field == null) {
      throw new IllegalArgumentException();
    }

    switch (field) {
    case IP:
      return isSetIp();
    case UA:
      return isSetUa();
    }
    throw new IllegalStateException();
  }

  @Override
  public boolean equals(Object that) {
    if (that == null)
      return false;
    if (that instanceof TDevice)
      return this.equals((TDevice)that);
    return false;
  }

  public boolean equals(TDevice that) {
    if (that == null)
      return false;

    boolean this_present_ip = true && this.isSetIp();
    boolean that_present_ip = true && that.isSetIp();
    if (this_present_ip || that_present_ip) {
      if (!(this_present_ip && that_present_ip))
        return false;
      if (!this.ip.equals(that.ip))
        return false;
    }

    boolean this_present_ua = true && this.isSetUa();
    boolean that_present_ua = true && that.isSetUa();
    if (this_present_ua || that_present_ua) {
      if (!(this_present_ua && that_present_ua))
        return false;
      if (!this.ua.equals(that.ua))
        return false;
    }

    return true;
  }

  @Override
  public int hashCode() {
    HashCodeBuilder builder = new HashCodeBuilder();

    boolean present_ip = true && (isSetIp());
    builder.append(present_ip);
    if (present_ip)
      builder.append(ip);

    boolean present_ua = true && (isSetUa());
    builder.append(present_ua);
    if (present_ua)
      builder.append(ua);

    return builder.toHashCode();
  }

  @Override
  public int compareTo(TDevice other) {
    if (!getClass().equals(other.getClass())) {
      return getClass().getName().compareTo(other.getClass().getName());
    }

    int lastComparison = 0;

    lastComparison = Boolean.valueOf(isSetIp()).compareTo(other.isSetIp());
    if (lastComparison != 0) {
      return lastComparison;
    }
    if (isSetIp()) {
      lastComparison = org.apache.thrift.TBaseHelper.compareTo(this.ip, other.ip);
      if (lastComparison != 0) {
        return lastComparison;
      }
    }
    lastComparison = Boolean.valueOf(isSetUa()).compareTo(other.isSetUa());
    if (lastComparison != 0) {
      return lastComparison;
    }
    if (isSetUa()) {
      lastComparison = org.apache.thrift.TBaseHelper.compareTo(this.ua, other.ua);
      if (lastComparison != 0) {
        return lastComparison;
      }
    }
    return 0;
  }

  public _Fields fieldForId(int fieldId) {
    return _Fields.findByThriftId(fieldId);
  }

  public void read(org.apache.thrift.protocol.TProtocol iprot) throws org.apache.thrift.TException {
    schemes.get(iprot.getScheme()).getScheme().read(iprot, this);
  }

  public void write(org.apache.thrift.protocol.TProtocol oprot) throws org.apache.thrift.TException {
    schemes.get(oprot.getScheme()).getScheme().write(oprot, this);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder("TDevice(");
    boolean first = true;

    if (isSetIp()) {
      sb.append("ip:");
      if (this.ip == null) {
        sb.append("null");
      } else {
        sb.append(this.ip);
      }
      first = false;
    }
    if (isSetUa()) {
      if (!first) sb.append(", ");
      sb.append("ua:");
      if (this.ua == null) {
        sb.append("null");
      } else {
        sb.append(this.ua);
      }
      first = false;
    }
    sb.append(")");
    return sb.toString();
  }

  public void validate() throws org.apache.thrift.TException {
    // check for required fields
    // check for sub-struct validity
  }

  private void writeObject(java.io.ObjectOutputStream out) throws java.io.IOException {
    try {
      write(new org.apache.thrift.protocol.TCompactProtocol(new org.apache.thrift.transport.TIOStreamTransport(out)));
    } catch (org.apache.thrift.TException te) {
      throw new java.io.IOException(te);
    }
  }

  private void readObject(java.io.ObjectInputStream in) throws java.io.IOException, ClassNotFoundException {
    try {
      read(new org.apache.thrift.protocol.TCompactProtocol(new org.apache.thrift.transport.TIOStreamTransport(in)));
    } catch (org.apache.thrift.TException te) {
      throw new java.io.IOException(te);
    }
  }

  private static class TDeviceStandardSchemeFactory implements SchemeFactory {
    public TDeviceStandardScheme getScheme() {
      return new TDeviceStandardScheme();
    }
  }

  private static class TDeviceStandardScheme extends StandardScheme<TDevice> {

    public void read(org.apache.thrift.protocol.TProtocol iprot, TDevice struct) throws org.apache.thrift.TException {
      org.apache.thrift.protocol.TField schemeField;
      iprot.readStructBegin();
      while (true)
      {
        schemeField = iprot.readFieldBegin();
        if (schemeField.type == org.apache.thrift.protocol.TType.STOP) { 
          break;
        }
        switch (schemeField.id) {
          case 1: // IP
            if (schemeField.type == org.apache.thrift.protocol.TType.STRING) {
              struct.ip = iprot.readString();
              struct.setIpIsSet(true);
            } else { 
              org.apache.thrift.protocol.TProtocolUtil.skip(iprot, schemeField.type);
            }
            break;
          case 2: // UA
            if (schemeField.type == org.apache.thrift.protocol.TType.STRING) {
              struct.ua = iprot.readString();
              struct.setUaIsSet(true);
            } else { 
              org.apache.thrift.protocol.TProtocolUtil.skip(iprot, schemeField.type);
            }
            break;
          default:
            org.apache.thrift.protocol.TProtocolUtil.skip(iprot, schemeField.type);
        }
        iprot.readFieldEnd();
      }
      iprot.readStructEnd();
      struct.validate();
    }

    public void write(org.apache.thrift.protocol.TProtocol oprot, TDevice struct) throws org.apache.thrift.TException {
      struct.validate();

      oprot.writeStructBegin(STRUCT_DESC);
      if (struct.ip != null) {
        if (struct.isSetIp()) {
          oprot.writeFieldBegin(IP_FIELD_DESC);
          oprot.writeString(struct.ip);
          oprot.writeFieldEnd();
        }
      }
      if (struct.ua != null) {
        if (struct.isSetUa()) {
          oprot.writeFieldBegin(UA_FIELD_DESC);
          oprot.writeString(struct.ua);
          oprot.writeFieldEnd();
        }
      }
      oprot.writeFieldStop();
      oprot.writeStructEnd();
    }

  }

  private static class TDeviceTupleSchemeFactory implements SchemeFactory {
    public TDeviceTupleScheme getScheme() {
      return new TDeviceTupleScheme();
    }
  }

  private static class TDeviceTupleScheme extends TupleScheme<TDevice> {

    @Override
    public void write(org.apache.thrift.protocol.TProtocol prot, TDevice struct) throws org.apache.thrift.TException {
      TTupleProtocol oprot = (TTupleProtocol) prot;
      BitSet optionals = new BitSet();
      if (struct.isSetIp()) {
        optionals.set(0);
      }
      if (struct.isSetUa()) {
        optionals.set(1);
      }
      oprot.writeBitSet(optionals, 2);
      if (struct.isSetIp()) {
        oprot.writeString(struct.ip);
      }
      if (struct.isSetUa()) {
        oprot.writeString(struct.ua);
      }
    }

    @Override
    public void read(org.apache.thrift.protocol.TProtocol prot, TDevice struct) throws org.apache.thrift.TException {
      TTupleProtocol iprot = (TTupleProtocol) prot;
      BitSet incoming = iprot.readBitSet(2);
      if (incoming.get(0)) {
        struct.ip = iprot.readString();
        struct.setIpIsSet(true);
      }
      if (incoming.get(1)) {
        struct.ua = iprot.readString();
        struct.setUaIsSet(true);
      }
    }
  }

}

