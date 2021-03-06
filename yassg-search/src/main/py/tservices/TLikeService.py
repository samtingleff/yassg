#
# Autogenerated by Thrift Compiler (0.11.0)
#
# DO NOT EDIT UNLESS YOU ARE SURE THAT YOU KNOW WHAT YOU ARE DOING
#
#  options string: py
#

from thrift.Thrift import TType, TMessageType, TFrozenDict, TException, TApplicationException
from thrift.protocol.TProtocol import TProtocolException
from thrift.TRecursive import fix_spec

import sys
import logging
from .ttypes import *
from thrift.Thrift import TProcessor
from thrift.transport import TTransport
all_structs = []


class Iface(object):
    def like(self, device, page):
        """
        Parameters:
         - device
         - page
        """
        pass

    def count(self, device, page):
        """
        Parameters:
         - device
         - page
        """
        pass


class Client(Iface):
    def __init__(self, iprot, oprot=None):
        self._iprot = self._oprot = iprot
        if oprot is not None:
            self._oprot = oprot
        self._seqid = 0

    def like(self, device, page):
        """
        Parameters:
         - device
         - page
        """
        self.send_like(device, page)
        self.recv_like()

    def send_like(self, device, page):
        self._oprot.writeMessageBegin('like', TMessageType.CALL, self._seqid)
        args = like_args()
        args.device = device
        args.page = page
        args.write(self._oprot)
        self._oprot.writeMessageEnd()
        self._oprot.trans.flush()

    def recv_like(self):
        iprot = self._iprot
        (fname, mtype, rseqid) = iprot.readMessageBegin()
        if mtype == TMessageType.EXCEPTION:
            x = TApplicationException()
            x.read(iprot)
            iprot.readMessageEnd()
            raise x
        result = like_result()
        result.read(iprot)
        iprot.readMessageEnd()
        if result.error is not None:
            raise result.error
        if result.sessionError is not None:
            raise result.sessionError
        return

    def count(self, device, page):
        """
        Parameters:
         - device
         - page
        """
        self.send_count(device, page)
        return self.recv_count()

    def send_count(self, device, page):
        self._oprot.writeMessageBegin('count', TMessageType.CALL, self._seqid)
        args = count_args()
        args.device = device
        args.page = page
        args.write(self._oprot)
        self._oprot.writeMessageEnd()
        self._oprot.trans.flush()

    def recv_count(self):
        iprot = self._iprot
        (fname, mtype, rseqid) = iprot.readMessageBegin()
        if mtype == TMessageType.EXCEPTION:
            x = TApplicationException()
            x.read(iprot)
            iprot.readMessageEnd()
            raise x
        result = count_result()
        result.read(iprot)
        iprot.readMessageEnd()
        if result.success is not None:
            return result.success
        if result.error is not None:
            raise result.error
        if result.sessionError is not None:
            raise result.sessionError
        raise TApplicationException(TApplicationException.MISSING_RESULT, "count failed: unknown result")


class Processor(Iface, TProcessor):
    def __init__(self, handler):
        self._handler = handler
        self._processMap = {}
        self._processMap["like"] = Processor.process_like
        self._processMap["count"] = Processor.process_count

    def process(self, iprot, oprot):
        (name, type, seqid) = iprot.readMessageBegin()
        if name not in self._processMap:
            iprot.skip(TType.STRUCT)
            iprot.readMessageEnd()
            x = TApplicationException(TApplicationException.UNKNOWN_METHOD, 'Unknown function %s' % (name))
            oprot.writeMessageBegin(name, TMessageType.EXCEPTION, seqid)
            x.write(oprot)
            oprot.writeMessageEnd()
            oprot.trans.flush()
            return
        else:
            self._processMap[name](self, seqid, iprot, oprot)
        return True

    def process_like(self, seqid, iprot, oprot):
        args = like_args()
        args.read(iprot)
        iprot.readMessageEnd()
        result = like_result()
        try:
            self._handler.like(args.device, args.page)
            msg_type = TMessageType.REPLY
        except TTransport.TTransportException:
            raise
        except TLikeException as error:
            msg_type = TMessageType.REPLY
            result.error = error
        except TSessionException as sessionError:
            msg_type = TMessageType.REPLY
            result.sessionError = sessionError
        except TApplicationException as ex:
            logging.exception('TApplication exception in handler')
            msg_type = TMessageType.EXCEPTION
            result = ex
        except Exception:
            logging.exception('Unexpected exception in handler')
            msg_type = TMessageType.EXCEPTION
            result = TApplicationException(TApplicationException.INTERNAL_ERROR, 'Internal error')
        oprot.writeMessageBegin("like", msg_type, seqid)
        result.write(oprot)
        oprot.writeMessageEnd()
        oprot.trans.flush()

    def process_count(self, seqid, iprot, oprot):
        args = count_args()
        args.read(iprot)
        iprot.readMessageEnd()
        result = count_result()
        try:
            result.success = self._handler.count(args.device, args.page)
            msg_type = TMessageType.REPLY
        except TTransport.TTransportException:
            raise
        except TLikeException as error:
            msg_type = TMessageType.REPLY
            result.error = error
        except TSessionException as sessionError:
            msg_type = TMessageType.REPLY
            result.sessionError = sessionError
        except TApplicationException as ex:
            logging.exception('TApplication exception in handler')
            msg_type = TMessageType.EXCEPTION
            result = ex
        except Exception:
            logging.exception('Unexpected exception in handler')
            msg_type = TMessageType.EXCEPTION
            result = TApplicationException(TApplicationException.INTERNAL_ERROR, 'Internal error')
        oprot.writeMessageBegin("count", msg_type, seqid)
        result.write(oprot)
        oprot.writeMessageEnd()
        oprot.trans.flush()

# HELPER FUNCTIONS AND STRUCTURES


class like_args(object):
    """
    Attributes:
     - device
     - page
    """


    def __init__(self, device=None, page=None,):
        self.device = device
        self.page = page

    def read(self, iprot):
        if iprot._fast_decode is not None and isinstance(iprot.trans, TTransport.CReadableTransport) and self.thrift_spec is not None:
            iprot._fast_decode(self, iprot, [self.__class__, self.thrift_spec])
            return
        iprot.readStructBegin()
        while True:
            (fname, ftype, fid) = iprot.readFieldBegin()
            if ftype == TType.STOP:
                break
            if fid == 1:
                if ftype == TType.STRUCT:
                    self.device = TDevice()
                    self.device.read(iprot)
                else:
                    iprot.skip(ftype)
            elif fid == 2:
                if ftype == TType.I64:
                    self.page = iprot.readI64()
                else:
                    iprot.skip(ftype)
            else:
                iprot.skip(ftype)
            iprot.readFieldEnd()
        iprot.readStructEnd()

    def write(self, oprot):
        if oprot._fast_encode is not None and self.thrift_spec is not None:
            oprot.trans.write(oprot._fast_encode(self, [self.__class__, self.thrift_spec]))
            return
        oprot.writeStructBegin('like_args')
        if self.device is not None:
            oprot.writeFieldBegin('device', TType.STRUCT, 1)
            self.device.write(oprot)
            oprot.writeFieldEnd()
        if self.page is not None:
            oprot.writeFieldBegin('page', TType.I64, 2)
            oprot.writeI64(self.page)
            oprot.writeFieldEnd()
        oprot.writeFieldStop()
        oprot.writeStructEnd()

    def validate(self):
        return

    def __repr__(self):
        L = ['%s=%r' % (key, value)
             for key, value in self.__dict__.items()]
        return '%s(%s)' % (self.__class__.__name__, ', '.join(L))

    def __eq__(self, other):
        return isinstance(other, self.__class__) and self.__dict__ == other.__dict__

    def __ne__(self, other):
        return not (self == other)
all_structs.append(like_args)
like_args.thrift_spec = (
    None,  # 0
    (1, TType.STRUCT, 'device', [TDevice, None], None, ),  # 1
    (2, TType.I64, 'page', None, None, ),  # 2
)


class like_result(object):
    """
    Attributes:
     - error
     - sessionError
    """


    def __init__(self, error=None, sessionError=None,):
        self.error = error
        self.sessionError = sessionError

    def read(self, iprot):
        if iprot._fast_decode is not None and isinstance(iprot.trans, TTransport.CReadableTransport) and self.thrift_spec is not None:
            iprot._fast_decode(self, iprot, [self.__class__, self.thrift_spec])
            return
        iprot.readStructBegin()
        while True:
            (fname, ftype, fid) = iprot.readFieldBegin()
            if ftype == TType.STOP:
                break
            if fid == 1:
                if ftype == TType.STRUCT:
                    self.error = TLikeException()
                    self.error.read(iprot)
                else:
                    iprot.skip(ftype)
            elif fid == 2:
                if ftype == TType.STRUCT:
                    self.sessionError = TSessionException()
                    self.sessionError.read(iprot)
                else:
                    iprot.skip(ftype)
            else:
                iprot.skip(ftype)
            iprot.readFieldEnd()
        iprot.readStructEnd()

    def write(self, oprot):
        if oprot._fast_encode is not None and self.thrift_spec is not None:
            oprot.trans.write(oprot._fast_encode(self, [self.__class__, self.thrift_spec]))
            return
        oprot.writeStructBegin('like_result')
        if self.error is not None:
            oprot.writeFieldBegin('error', TType.STRUCT, 1)
            self.error.write(oprot)
            oprot.writeFieldEnd()
        if self.sessionError is not None:
            oprot.writeFieldBegin('sessionError', TType.STRUCT, 2)
            self.sessionError.write(oprot)
            oprot.writeFieldEnd()
        oprot.writeFieldStop()
        oprot.writeStructEnd()

    def validate(self):
        return

    def __repr__(self):
        L = ['%s=%r' % (key, value)
             for key, value in self.__dict__.items()]
        return '%s(%s)' % (self.__class__.__name__, ', '.join(L))

    def __eq__(self, other):
        return isinstance(other, self.__class__) and self.__dict__ == other.__dict__

    def __ne__(self, other):
        return not (self == other)
all_structs.append(like_result)
like_result.thrift_spec = (
    None,  # 0
    (1, TType.STRUCT, 'error', [TLikeException, None], None, ),  # 1
    (2, TType.STRUCT, 'sessionError', [TSessionException, None], None, ),  # 2
)


class count_args(object):
    """
    Attributes:
     - device
     - page
    """


    def __init__(self, device=None, page=None,):
        self.device = device
        self.page = page

    def read(self, iprot):
        if iprot._fast_decode is not None and isinstance(iprot.trans, TTransport.CReadableTransport) and self.thrift_spec is not None:
            iprot._fast_decode(self, iprot, [self.__class__, self.thrift_spec])
            return
        iprot.readStructBegin()
        while True:
            (fname, ftype, fid) = iprot.readFieldBegin()
            if ftype == TType.STOP:
                break
            if fid == 1:
                if ftype == TType.STRUCT:
                    self.device = TDevice()
                    self.device.read(iprot)
                else:
                    iprot.skip(ftype)
            elif fid == 2:
                if ftype == TType.I64:
                    self.page = iprot.readI64()
                else:
                    iprot.skip(ftype)
            else:
                iprot.skip(ftype)
            iprot.readFieldEnd()
        iprot.readStructEnd()

    def write(self, oprot):
        if oprot._fast_encode is not None and self.thrift_spec is not None:
            oprot.trans.write(oprot._fast_encode(self, [self.__class__, self.thrift_spec]))
            return
        oprot.writeStructBegin('count_args')
        if self.device is not None:
            oprot.writeFieldBegin('device', TType.STRUCT, 1)
            self.device.write(oprot)
            oprot.writeFieldEnd()
        if self.page is not None:
            oprot.writeFieldBegin('page', TType.I64, 2)
            oprot.writeI64(self.page)
            oprot.writeFieldEnd()
        oprot.writeFieldStop()
        oprot.writeStructEnd()

    def validate(self):
        return

    def __repr__(self):
        L = ['%s=%r' % (key, value)
             for key, value in self.__dict__.items()]
        return '%s(%s)' % (self.__class__.__name__, ', '.join(L))

    def __eq__(self, other):
        return isinstance(other, self.__class__) and self.__dict__ == other.__dict__

    def __ne__(self, other):
        return not (self == other)
all_structs.append(count_args)
count_args.thrift_spec = (
    None,  # 0
    (1, TType.STRUCT, 'device', [TDevice, None], None, ),  # 1
    (2, TType.I64, 'page', None, None, ),  # 2
)


class count_result(object):
    """
    Attributes:
     - success
     - error
     - sessionError
    """


    def __init__(self, success=None, error=None, sessionError=None,):
        self.success = success
        self.error = error
        self.sessionError = sessionError

    def read(self, iprot):
        if iprot._fast_decode is not None and isinstance(iprot.trans, TTransport.CReadableTransport) and self.thrift_spec is not None:
            iprot._fast_decode(self, iprot, [self.__class__, self.thrift_spec])
            return
        iprot.readStructBegin()
        while True:
            (fname, ftype, fid) = iprot.readFieldBegin()
            if ftype == TType.STOP:
                break
            if fid == 0:
                if ftype == TType.I32:
                    self.success = iprot.readI32()
                else:
                    iprot.skip(ftype)
            elif fid == 1:
                if ftype == TType.STRUCT:
                    self.error = TLikeException()
                    self.error.read(iprot)
                else:
                    iprot.skip(ftype)
            elif fid == 2:
                if ftype == TType.STRUCT:
                    self.sessionError = TSessionException()
                    self.sessionError.read(iprot)
                else:
                    iprot.skip(ftype)
            else:
                iprot.skip(ftype)
            iprot.readFieldEnd()
        iprot.readStructEnd()

    def write(self, oprot):
        if oprot._fast_encode is not None and self.thrift_spec is not None:
            oprot.trans.write(oprot._fast_encode(self, [self.__class__, self.thrift_spec]))
            return
        oprot.writeStructBegin('count_result')
        if self.success is not None:
            oprot.writeFieldBegin('success', TType.I32, 0)
            oprot.writeI32(self.success)
            oprot.writeFieldEnd()
        if self.error is not None:
            oprot.writeFieldBegin('error', TType.STRUCT, 1)
            self.error.write(oprot)
            oprot.writeFieldEnd()
        if self.sessionError is not None:
            oprot.writeFieldBegin('sessionError', TType.STRUCT, 2)
            self.sessionError.write(oprot)
            oprot.writeFieldEnd()
        oprot.writeFieldStop()
        oprot.writeStructEnd()

    def validate(self):
        return

    def __repr__(self):
        L = ['%s=%r' % (key, value)
             for key, value in self.__dict__.items()]
        return '%s(%s)' % (self.__class__.__name__, ', '.join(L))

    def __eq__(self, other):
        return isinstance(other, self.__class__) and self.__dict__ == other.__dict__

    def __ne__(self, other):
        return not (self == other)
all_structs.append(count_result)
count_result.thrift_spec = (
    (0, TType.I32, 'success', None, None, ),  # 0
    (1, TType.STRUCT, 'error', [TLikeException, None], None, ),  # 1
    (2, TType.STRUCT, 'sessionError', [TSessionException, None], None, ),  # 2
)
fix_spec(all_structs)
del all_structs

