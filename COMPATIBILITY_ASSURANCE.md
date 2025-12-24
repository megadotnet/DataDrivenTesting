# 兼容性保障体系设计 (Compatibility Assurance System Design)

## 1. 目标
确保 DataDrivenTesting 框架在升级到 Java 21 后，仍能保持对旧有测试数据的兼容性，并确保跨平台运行能力。

## 2. 静态代码分析
*   **工具**: 集成 Maven Enforcer Plugin，强制要求 Java 版本 >= 21。
*   **规则**: 禁止使用 `sun.misc.Unsafe` 等内部 API（Java 21 强封装策略）。

## 3. 跨平台兼容性
*   **路径隔离**: 所有文件路径操作必须使用 `java.io.File.separator` 或 `java.nio.file.Paths`，严禁硬编码 `\` 或 `/`。
    *   *实施*: 已在 `Base.java` 中完成改造。
*   **字符编码**: 统一项目编码为 `UTF-8`，避免在 Windows/Linux 间出现中文乱码。

## 4. API 兼容性适配层
针对 POI 和 Selenium 的破坏性变更，建议构建简单的适配层（Adapter Pattern），但在本项目中直接重构更为高效。
*   **POI Adapter**: 如需支持旧版 Excel 读写逻辑，可封装 `ExcelUtils` 类屏蔽 `HSSF` vs `XSSF` 细节。

## 5. 持续集成 (CI) 门禁
*   建议配置 GitHub Actions 或 Jenkins Pipeline。
*   **Matrix Testing**:
    *   OS: Ubuntu-latest, Windows-latest
    *   JDK: 21, 17 (验证向后兼容性)

## 6. 浏览器兼容性
*   Selenium 4 默认遵循 W3C WebDriver 标准。
*   对于旧版浏览器（如 IE），Selenium 4 已降级支持或不再支持，建议全面迁移至 Edge/Chrome/Firefox。
