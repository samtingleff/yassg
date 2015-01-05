#!/usr/bin/env python
import argparse
from tservices import *
from thrift import Thrift
from thrift.transport import TSocket
from thrift.transport import TTransport
from thrift.protocol import TCompactProtocol

parser = argparse.ArgumentParser(description='Conduct a search.')
parser.add_argument("--host", help="Server hostname", default="localhost")
parser.add_argument("--port", help="Server port", default=9999, type=int)
parser.add_argument("query", help="Search query")
args = parser.parse_args()

transport = TSocket.TSocket(args.host, args.port)
transport = TTransport.TBufferedTransport(transport)
protocol = TCompactProtocol.TCompactProtocol(transport)
client = TSearchService.Client(protocol)
transport.open()
result = client.search(args.query, 10, None)
transport.close()

print result
