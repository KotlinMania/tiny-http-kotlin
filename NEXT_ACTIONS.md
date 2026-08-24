# Immediate Actions - High-Value Files

Based on AST analysis, here are the concrete next steps.

## Summary

- **Files Present:** 18/18 (100.0%)
- **Function parity:** 60/171 matched (target 134) — 35.1%
- **Class/type parity:** 33/52 matched (target 73) — 63.5%
- **Combined symbol parity:** 93/223 matched (target 207) — 41.7%
- **Average inline-code cosine:** 0.22 (function body across 15 matched files)
- **Average documentation cosine:** 0.39 (doc text across 15 matched files)
- **Cheat-zeroed Files:** 4
- **Critical Issues:** 17 files with <0.60 function similarity

## Priority 1: Fix Incomplete High-Dependency Files

No incomplete high-dependency files detected.

## Priority 2: Port Missing High-Value Files

Critical missing files (>10 dependencies):

No missing high-value files detected.

## Detailed Work Items

Every matched file is listed below with function and type symbol parity.

### 1. connection

- **Target:** `tinyhttp.Connection`
- **Similarity:** 0.10
- **Dependents:** 4
- **Priority Score:** 4111909.0
- **Functions:** 4/15 matched (target 9)
- **Missing functions:** `local_addr`, `accept`, `from`, `read`, `write`, `flush`, `shutdown`, `try_clone`, `from_socket_addrs`, `bind`, `fmt`
- **Types:** 4/4 matched (target 8)
- **Missing types:** _none_

### 2. util.refined_tcp_stream

- **Target:** `util.RefinedTcpStream [STUB]`
- **Similarity:** 0.00
- **Dependents:** 3
- **Priority Score:** 3091210.0
- **Functions:** 1/10 matched (target 5)
- **Missing functions:** `clone`, `from`, `secure`, `peer_addr`, `shutdown`, `read`, `write`, `flush`, `drop`
- **Types:** 2/2 matched (target 5)
- **Missing types:** _none_

### 3. util.custom_stream

- **Target:** `util.CustomStream`
- **Similarity:** 0.24
- **Dependents:** 2
- **Priority Score:** 2030507.6
- **Functions:** 1/4 matched (target 1)
- **Missing functions:** `read`, `write`, `flush`
- **Types:** 1/1 matched
- **Missing types:** _none_

### 4. util.messages_queue

- **Target:** `util.MessagesQueue`
- **Similarity:** 0.47
- **Dependents:** 2
- **Priority Score:** 2010805.4
- **Functions:** 5/6 matched (target 8)
- **Missing functions:** `pop_timeout`
- **Types:** 2/2 matched (target 5)
- **Missing types:** _none_

### 5. request

- **Target:** `tinyhttp.Request`
- **Similarity:** 0.08
- **Dependents:** 1
- **Priority Score:** 1243009.1
- **Functions:** 4/26 matched (target 7)
- **Missing functions:** `read`, `write`, `flush`, `drop`, `from`, `secure`, `method`, `headers`, `http_version`, `body_length`, `remote_addr`, `upgrade`, `into_writer`, `extract_writer_impl`, `extract_reader_impl`, `respond_impl`, `ignore_client_closing_errors`, `with_notify_sender`, `fmt`, `must_be_send`, `f`, `bar`
- **Types:** 2/4 matched (target 5)
- **Missing types:** `NotifyOnDrop`, `ReadWrite`
- **Tests:** 0/3 matched

### 6. util.task_pool

- **Target:** `util.TaskPool`
- **Similarity:** 0.08
- **Dependents:** 1
- **Priority Score:** 1040709.2
- **Functions:** 2/4 matched (target 3)
- **Missing functions:** `drop`, `add_thread`
- **Types:** 1/3 matched (target 2)
- **Missing types:** `Sharing`, `Registration`

### 7. util.equal_reader

- **Target:** `util.EqualReader`
- **Similarity:** 0.26
- **Dependents:** 1
- **Priority Score:** 1020607.4
- **Functions:** 3/5 matched (target 4)
- **Missing functions:** `read`, `drop`
- **Types:** 1/1 matched (target 2)
- **Missing types:** _none_
- **Tests:** 2/2 matched

### 8. util.fused_reader

- **Target:** `util.FusedReader`
- **Similarity:** 0.45
- **Dependents:** 1
- **Priority Score:** 1020505.4
- **Functions:** 2/4 matched (target 5)
- **Missing functions:** `read`, `read_vectored`
- **Types:** 1/1 matched (target 2)
- **Missing types:** _none_

### 9. ssl

- **Target:** `ssl.Ssl [ZERO]`
- **Similarity:** 0.00
- **Dependents:** 1
- **Priority Score:** 1000010.0
- **Functions:** 0/0 matched (target 2)
- **Missing functions:** _none_
- **Types:** 0/0 matched (target 3)
- **Missing types:** _none_

### 10. response

- **Target:** `tinyhttp.Response`
- **Similarity:** 0.35
- **Dependents:** 0
- **Priority Score:** 152706.5
- **Functions:** 10/23 matched (target 18)
- **Missing functions:** `from_str`, `build_date_header`, `write_message_header`, `new`, `chunked_threshold`, `raw_print`, `status_code`, `data_length`, `headers`, `boxed`, `from_file`, `new_empty`, `clone`
- **Types:** 2/4 matched (target 3)
- **Missing types:** `ResponseBox`, `Err`

### 11. lib

- **Target:** `tinyhttp.Server`
- **Similarity:** 0.15
- **Dependents:** 0
- **Priority Score:** 152308.5
- **Functions:** 5/15 matched (target 9)
- **Missing functions:** `from`, `http_unix`, `new`, `from_listener`, `incoming_requests`, `server_addr`, `num_connections`, `recv_timeout`, `next`, `drop`
- **Types:** 3/8 matched (target 4)
- **Missing types:** `Message`, `MustBeShareDummy`, `IncomingRequests`, `SslContext`, `Item`

### 12. ssl.openssl

- **Target:** `ssl.OpenSsl`
- **Similarity:** 0.02
- **Dependents:** 0
- **Priority Score:** 101209.8
- **Functions:** 1/9 matched (target 2)
- **Missing functions:** `peer_addr`, `shutdown`, `clone`, `read`, `write`, `flush`, `accept`, `from`
- **Types:** 1/3 matched (target 1)
- **Missing types:** `OpenSslStream`, `SplitOpenSslStream`

### 13. ssl.rustls

- **Target:** `ssl.Rustls`
- **Similarity:** 0.05
- **Dependents:** 0
- **Priority Score:** 91109.5
- **Functions:** 1/9 matched (target 2)
- **Missing functions:** `peer_addr`, `shutdown`, `clone`, `read`, `write`, `flush`, `accept`, `from`
- **Types:** 1/2 matched (target 1)
- **Missing types:** `RustlsStream`

### 14. util.sequential

- **Target:** `util.Sequential`
- **Similarity:** 0.18
- **Dependents:** 0
- **Priority Score:** 81308.2
- **Functions:** 1/6 matched
- **Missing functions:** `next`, `read`, `write`, `flush`, `drop`
- **Types:** 4/7 matched (target 5)
- **Missing types:** `SequentialReaderBuilderInner`, `SequentialReaderInner`, `Item`

### 15. client

- **Target:** `tinyhttp.Client`
- **Similarity:** 0.09
- **Dependents:** 0
- **Priority Score:** 81109.1
- **Functions:** 1/8 matched (target 2)
- **Missing functions:** `secure`, `read_next_line`, `read`, `next`, `parse_http_version`, `parse_request_line`, `test_parse_request_line`
- **Types:** 2/3 matched (target 7)
- **Missing types:** `Item`
- **Tests:** 0/1 matched

### 16. common

- **Target:** `tinyhttp.Common [STUB]`
- **Similarity:** 0.00
- **Dependents:** 0
- **Priority Score:** 72110.0
- **Functions:** 9/15 matched (target 40)
- **Missing functions:** `from`, `as_ref`, `eq`, `partial_cmp`, `fmt`, `cmp`
- **Types:** 5/6 matched (target 16)
- **Missing types:** `Err`
- **Tests:** 4/4 matched

### 17. test

- **Target:** `tinyhttp.TestRequest`
- **Similarity:** 0.73
- **Dependents:** 0
- **Priority Score:** 21102.7
- **Functions:** 8/10 matched (target 9)
- **Missing functions:** `from`, `default`
- **Types:** 1/1 matched (target 2)
- **Missing types:** _none_

### 18. util.mod

- **Target:** `tinyhttp.Util [STUB]`
- **Similarity:** 0.00
- **Dependents:** 0
- **Priority Score:** 210.0
- **Functions:** 2/2 matched
- **Missing functions:** _none_
- **Types:** 0/0 matched (target 1)
- **Missing types:** _none_
- **Tests:** 1/1 matched

## Success Criteria

For each file to be considered "complete":
- **Similarity ≥ 0.85** (Excellent threshold)
- All public APIs ported
- All tests ported
- Documentation ported
- port-lint header present

