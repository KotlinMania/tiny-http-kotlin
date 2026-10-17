# Immediate Actions - High-Value Files

Based on AST analysis, here are the concrete next steps.

## Summary

- **Files Present:** 15/18 (83.3%)
- **Function parity:** 147/195 matched (target 221) — 75.4%
- **Class/type parity:** 39/44 matched (target 80) — 88.6%
- **Combined symbol parity:** 186/239 matched (target 301) — 77.8%
- **Average inline-code cosine:** 0.12 (function body across 14 matched files)
- **Average documentation cosine:** 0.54 (doc text across 14 matched files)
- **Cheat-zeroed Files:** 1
- **Critical Issues:** 15 files with <0.60 function similarity
- **Needs Review:** 0 files with 0.60-0.84 function similarity
- **Excellent:** 0 files with >=0.85 function similarity

## Priority 1: Fix Incomplete High-Dependency Files

No incomplete high-dependency files detected.

## Priority 2: Port Missing High-Value Files

Critical missing files (>10 dependencies):

No missing high-value files detected.

## Detailed Work Items

Every matched file is listed below with function and type symbol parity.

### 1. connection

- **Target:** `tinyhttp.Connection`
- **Similarity:** 0.07
- **Dependents:** 4
- **Priority Score:** 4012509.2
- **Functions:** 20/21 matched (target 27)
- **Missing functions:** `to_unix`
- **Types:** 4/4 matched (target 9)
- **Missing types:** _none_
- **Lint issues:** 1

### 2. util.refined_tcp_stream

- **Target:** `util.RefinedTcpStream [STUB]`
- **Similarity:** 0.00
- **Dependents:** 3
- **Priority Score:** 3001710.0
- **Functions:** 15/15 matched (target 19)
- **Missing functions:** _none_
- **Types:** 2/2 matched (target 5)
- **Missing types:** _none_

### 3. util.messages_queue

- **Target:** `util.MessagesQueue`
- **Similarity:** 0.09
- **Dependents:** 2
- **Priority Score:** 2000809.1
- **Functions:** 6/6 matched (target 10)
- **Missing functions:** _none_
- **Types:** 2/2 matched (target 5)
- **Missing types:** _none_

### 4. util.custom_stream

- **Target:** `util.CustomStream`
- **Similarity:** 0.23
- **Dependents:** 2
- **Priority Score:** 2000507.8
- **Functions:** 4/4 matched (target 5)
- **Missing functions:** _none_
- **Types:** 1/1 matched
- **Missing types:** _none_

### 5. request

- **Target:** `tinyhttp.Request`
- **Similarity:** 0.08
- **Dependents:** 1
- **Priority Score:** 1083109.1
- **Functions:** 19/27 matched
- **Missing functions:** `secure`, `method`, `headers`, `http_version`, `body_length`, `remote_addr`, `f`, `bar`
- **Types:** 4/4 matched (target 7)
- **Missing types:** _none_
- **Tests:** 1/3 matched

### 6. util.task_pool

- **Target:** `util.TaskPool`
- **Similarity:** 0.02
- **Dependents:** 1
- **Priority Score:** 1000909.8
- **Functions:** 6/6 matched (target 7)
- **Missing functions:** _none_
- **Types:** 3/3 matched (target 4)
- **Missing types:** _none_

### 7. util.equal_reader

- **Target:** `util.EqualReader`
- **Similarity:** 0.12
- **Dependents:** 1
- **Priority Score:** 1000608.8
- **Functions:** 5/5 matched (target 6)
- **Missing functions:** _none_
- **Types:** 1/1 matched (target 2)
- **Missing types:** _none_
- **Tests:** 2/2 matched

### 8. util.fused_reader

- **Target:** `util.FusedReader`
- **Similarity:** 0.17
- **Dependents:** 1
- **Priority Score:** 1000508.3
- **Functions:** 4/4 matched (target 7)
- **Missing functions:** _none_
- **Types:** 1/1 matched (target 2)
- **Missing types:** _none_

### 9. common

- **Target:** `tinyhttp.Common`
- **Similarity:** 0.07
- **Dependents:** 0
- **Priority Score:** 244209.3
- **Functions:** 13/36 matched (target 40)
- **Missing functions:** `from`, `from`, `from`, `from`, `from`, `from`, `as_ref`, `eq`, `eq`, `partial_cmp`, `partial_cmp`, `fmt`, `fmt`, `eq`, `fmt`, `fmt`, `cmp`, `partial_cmp`, `eq`, `eq`, `partial_cmp`, `partial_cmp`, `from`
- **Types:** 5/6 matched (target 16)
- **Missing types:** `Err`
- **Tests:** 4/4 matched

### 10. response

- **Target:** `tinyhttp.Response`
- **Similarity:** 0.15
- **Dependents:** 0
- **Priority Score:** 92708.5
- **Functions:** 16/23 matched (target 24)
- **Missing functions:** `from_str`, `build_date_header`, `chunked_threshold`, `status_code`, `data_length`, `headers`, `from_file`
- **Types:** 2/4 matched (target 3)
- **Missing types:** `ResponseBox`, `Err`

### 11. client

- **Target:** `tinyhttp.Client`
- **Similarity:** 0.02
- **Dependents:** 0
- **Priority Score:** 81109.8
- **Functions:** 1/8 matched (target 2)
- **Missing functions:** `secure`, `read_next_line`, `read`, `next`, `parse_http_version`, `parse_request_line`, `test_parse_request_line`
- **Types:** 2/3 matched (target 7)
- **Missing types:** `Item`
- **Tests:** 0/1 matched

### 12. test

- **Target:** `tinyhttp.TestRequest`
- **Similarity:** 0.28
- **Dependents:** 0
- **Priority Score:** 21107.2
- **Functions:** 8/10 matched (target 9)
- **Missing functions:** `from`, `default`
- **Types:** 1/1 matched (target 2)
- **Missing types:** _none_

### 13. util.sequential

- **Target:** `util.Sequential`
- **Similarity:** 0.07
- **Dependents:** 0
- **Priority Score:** 11609.3
- **Functions:** 9/9 matched (target 13)
- **Missing functions:** _none_
- **Types:** 6/7 matched (target 12)
- **Missing types:** `Item`

### 14. ssl.openssl

- **Target:** `ssl.OpenSsl`
- **Similarity:** 0.19
- **Dependents:** 0
- **Priority Score:** 1508.1
- **Functions:** 12/12 matched (target 14)
- **Missing functions:** _none_
- **Types:** 3/3 matched
- **Missing types:** _none_

### 15. ssl.rustls

- **Target:** `ssl.Rustls`
- **Similarity:** 0.16
- **Dependents:** 0
- **Priority Score:** 1108.4
- **Functions:** 9/9 matched (target 11)
- **Missing functions:** _none_
- **Types:** 2/2 matched
- **Missing types:** _none_

## Success Criteria

For each file to be considered "complete":
- **Similarity ≥ 0.85** (Excellent threshold)
- All public APIs ported
- All tests ported
- Documentation ported
- port-lint header present

## Reexport / Wiring Modules

These files match `reexport_modules` patterns in `.ast_distance_config.json`. They are filtered out of
normal priority and missing-file ladders because they are wiring
modules, not direct logic ports. Consult them for call-site routing;
do not treat them as the next implementation target by default.

### Missing

| Source | Expected target | Deps | Source path | Expected path |
|--------|-----------------|------|-------------|---------------|
| `lib` | `Lib` | 0 | `lib.rs` | `Lib.kt` |
| `util.mod` | `util.Mod` | 0 | `util/mod.rs` | `util/Mod.kt` |

