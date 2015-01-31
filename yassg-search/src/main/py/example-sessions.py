#!/usr/bin/env python
import argparse
from tservices import *
from thrift import Thrift
from thrift.transport import TSocket
from thrift.transport import TTransport
from thrift.protocol import TCompactProtocol

parser = argparse.ArgumentParser(description='Create a session.')
parser.add_argument("--host", help="Server hostname", default="localhost")
parser.add_argument("--port", help="Server port", default=9998, type=int)
parser.add_argument("ip", help="IP address")
parser.add_argument("ua", help="User agent")
args = parser.parse_args()

transport = TSocket.TSocket(args.host, args.port)
transport = TTransport.TBufferedTransport(transport)
protocol = TCompactProtocol.TCompactProtocol(transport)
client = TSessionService.Client(protocol)
transport.open()
result = client.create(ttypes.TDevice(args.ip, args.ua, None))
transport.close()

print result
