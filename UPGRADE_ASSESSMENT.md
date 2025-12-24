# Java版本升级可行性评估报告

## 1. 项目概况
*   **项目名称**: DataDrivenTesting
*   **当前版本**: Java 1.8 (Maven Source/Target)
*   **目标版本**: Java 21 LTS
*   **核心功能**: 基于 Selenium 和 TestNG 的数据驱动测试框架

## 2. 现状与风险评估

### 2.1 依赖风险
| 依赖项 | 当前版本 | 风险描述 | 建议升级版本 |
| :--- | :--- | :--- | :--- |
| **Java** | 1.8 | 已过时，无法利用新特性，性能受限。 | **21 LTS** |
| **Selenium** | 2.43.0 | 极度老旧 (2014年)。不支持现代浏览器，`HtmlUnitDriver` 架构变更，API 与 W3C 标准不兼容。 | **4.23.0** |
| **TestNG** | 6.8 | 可能存在与 Java 9+ 模块系统的兼容性问题。 | **7.10.0** |
| **POI** | 4.1.1 / 3.17 | 版本不一致 (`poi` 4.1.1 vs `poi-ooxml` 3.17)，会导致运行时 `ClassNotFoundException`。API (`Cell.CELL_TYPE_*`) 已移除。 | **5.2.3** |
| **Jersey** | 1.17.1 | JAX-RS 1.x 实现，且使用 `com.sun.jersey` 包名，与 Jakarta EE 不兼容。 | 移除或迁移至 Jersey 3.x |

### 2.2 代码兼容性风险
*   **硬编码路径**: 代码中包含大量 Windows 风格路径 (e.g., `E:\\Program Files...`, `src\\test\\resources\\`)，在 Linux/CI 环境下会失败。
*   **API 废弃**:
    *   Selenium: `implicitlyWait(long, TimeUnit)` 变更为 `implicitlyWait(Duration)`.
    *   POI: `Cell.CELL_TYPE_STRING` 已移除，需迁移至 `CellType.STRING`.
*   **驱动管理**: 依赖本地手动下载的 `exe` 驱动，难以维护。建议迁移至 Selenium Manager (内置于 Selenium 4.6+)。

## 3. 升级策略 (三维平衡)
*   **兼容性**: 优先修复 POI 和 Selenium 的 API 破坏性变更。处理路径分隔符问题以支持跨平台。
*   **性能**: 利用 Java 21 的 G1/ZGC 优化。升级 Selenium 通信协议至 W3C 标准。
*   **工程**: 引入 Selenium Manager 自动化驱动管理，移除 `driver/` 目录下的二进制文件。

## 4. 工作量预估
*   **依赖升级**: 0.5 人天
*   **代码重构**: 0.5 人天 (修复编译错误与路径问题)
*   **回归测试**: 0.5 人天
*   **总计**: 1.5 人天

## 5. 结论
升级至 Java 21 可行且必要。主要挑战在于 Selenium 2 到 4 的跨越式升级，以及 POI 的 API 适配。风险可控，收益包括更好的稳定性、跨平台能力及现代化的开发体验。
