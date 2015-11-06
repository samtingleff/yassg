namespace java com.tingleff.yassg.search.types
namespace py tservices

enum TSortFieldType {
 IndexOrder = 1,
 Double = 2,
 Float = 3,
 Integer = 4,
 Long = 5,
 Relevance = 6,
 String = 7
}

struct TSortField {
 1: required string field,
 2: required TSortFieldType type,
 3: optional bool reverse
}

struct TSort {
 1: list<TSortField> sortFields
}

// 
struct TExplainTerm {
 1: optional string field,
 2: optional string value,
 3: optional double score
}

// explains why a particular result was included
struct TExplain {
 1: optional list<TExplainTerm> terms
}

// an individual search result
struct TSearchDoc {
 1: optional i32 docId,
 2: optional map<string, string> fields,
 3: optional TExplain explain
}

struct TSearchResult {
 1: list<TSearchDoc> hits
}

struct TDeviceId {
 1: optional i16 version,
 2: optional i64 id,
 3: optional string signature
}

struct TDevice {
 1: optional string ip,
 2: optional string ua,
 3: optional TDeviceId id
}

exception TSearchException {
}

exception TSessionException {
}

exception TLikeException {
}

service TSearchService {
 TSearchResult search(1: string query, 2: i32 n, 3: TSort sorting) throws (1: TSearchException error),
 TSearchResult similar(1: string query, 2: i32 n, 3: TSort sorting) throws (1: TSearchException error),
 void reopen() throws (1: TSearchException error)
}

service TSessionService {
 TDeviceId create(1: TDevice device) throws (1: TSessionException error),
 TDeviceId validate(1: TDevice device) throws (1: TSessionException error)
}

service TLikeService {
 void like(1: TDevice device, 2: i64 page) throws (1: TLikeException error),
 i64 count(1: TDevice device, 2: i64 page) throws (1: TLikeException error)
}
