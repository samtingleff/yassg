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

struct TSearchDoc {
 1: optional i32 docId,
 2: optional map<string, string> fields
}

struct TSearchResult {
 1: list<TSearchDoc> hits
}

exception TSearchException {
}

service TSearchService {
 TSearchResult search(1: string query, 2: i32 n, 3: TSort sorting) throws (1: TSearchException error),
 TSearchResult similar(1: string query, 2: i32 n, 3: TSort sorting) throws (1: TSearchException error),
 void reopen() throws (1: TSearchException error)
}
