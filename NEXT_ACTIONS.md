# Immediate Actions - High-Value Files

Based on AST analysis, here are the concrete next steps.

## Summary

- **Files Present:** 4/18 (22.2%)
- **Function parity:** 29/176 matched (target 69) — 16.5%
- **Class/type parity:** 8/44 matched (target 22) — 18.2%
- **Combined symbol parity:** 37/220 matched (target 91) — 16.8%
- **Average inline-code cosine:** 0.54 (function body across 2 matched files)
- **Average documentation cosine:** 0.68 (doc text across 2 matched files)
- **Cheat-zeroed Files:** 2
- **Critical Issues:** 3 files with <0.60 function similarity

## Priority 1: Fix Incomplete High-Dependency Files

No incomplete high-dependency files detected.

## Priority 2: Port Missing High-Value Files

Critical missing files (>10 dependencies):

No missing high-value files detected.

## Detailed Work Items

Every matched file is listed below with function and type symbol parity.

### 1. response

- **Target:** `tinyhttp.Response`
- **Similarity:** 0.35
- **Dependents:** 0
- **Priority Score:** 152706.5
- **Functions:** 10/23 matched (target 18)
- **Missing functions:** `from_str`, `build_date_header`, `write_message_header`, `new`, `chunked_threshold`, `raw_print`, `status_code`, `data_length`, `headers`, `boxed`, `from_file`, `new_empty`, `clone`
- **Types:** 2/4 matched (target 3)
- **Missing types:** `ResponseBox`, `Err`

### 2. common

- **Target:** `tinyhttp.Common [STUB]`
- **Similarity:** 0.00
- **Dependents:** 0
- **Priority Score:** 72110.0
- **Functions:** 9/15 matched (target 40)
- **Missing functions:** `from`, `as_ref`, `eq`, `partial_cmp`, `fmt`, `cmp`
- **Types:** 5/6 matched (target 16)
- **Missing types:** `Err`
- **Tests:** 4/4 matched

### 3. test

- **Target:** `tinyhttp.TestRequest`
- **Similarity:** 0.73
- **Dependents:** 0
- **Priority Score:** 21102.7
- **Functions:** 8/10 matched (target 9)
- **Missing functions:** `from`, `default`
- **Types:** 1/1 matched (target 2)
- **Missing types:** _none_

### 4. util.mod

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

## Reexport / Wiring Modules

These files match `reexport_modules` patterns in `.ast_distance_config.json`. They are filtered out of
normal priority and missing-file ladders because they are wiring
modules, not direct logic ports. Consult them for call-site routing;
do not treat them as the next implementation target by default.

### Missing

| Source | Expected target | Deps | Source path | Expected path |
|--------|-----------------|------|-------------|---------------|
| `lib` | `Lib` | 0 | `lib.rs` | `Lib.kt` |

