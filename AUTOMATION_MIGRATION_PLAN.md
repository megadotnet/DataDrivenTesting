# 自动化迁移实施方案 (Automation Migration Implementation Plan)

## 1. 迁移概览
本方案详细描述了将 DataDrivenTesting 项目从 Java 1.8 迁移至 Java 21 LTS 的实施步骤、技术细节及验证方法。

## 2. 核心组件升级矩阵

| 组件 | 原版本 | 目标版本 | 变更说明 |
| :--- | :--- | :--- | :--- |
| **JDK** | 1.8 | **21 LTS** | 启用新版垃圾回收器 (G1/ZGC)，模块化支持，性能大幅提升。 |
| **Selenium** | 2.43.0 | **4.23.0** | 架构重构，移除 Selenium RC，标准化 W3C 协议。`HtmlUnitDriver` 独立组件化。 |
| **TestNG** | 6.8 | **7.10.0** | 更好的并行执行支持，修复与新版 JDK 的兼容性。 |
| **Apache POI** | 4.1.1/3.17 | **5.2.3** | 统一版本，修复 API 兼容性，移除过时常量 (`CELL_TYPE_*`)。 |
| **Logback** | 1.2.3 | **1.4.14** | 适配 Java 11+ 环境。 |

## 3. 代码改造详情

### 3.1 基础设施层 (`Base.java`)
*   **驱动管理**: 移除 Windows 专用的硬编码 `driver/*.exe` 路径，推荐使用 Selenium Manager 或环境变量配置驱动。
*   **等待机制**: 将 `driver.manage().timeouts().implicitlyWait(TIME_OUT, TimeUnit.SECONDS)` 迁移至 `implicitlyWait(Duration.ofSeconds(TIME_OUT))`。
*   **POI 适配**: 将 `Cell.CELL_TYPE_STRING` 替换为 `CellType.STRING`。
*   **路径处理**: 替换硬编码的 `\\` 路径分隔符为 `File.separator`，增强跨平台兼容性。

### 3.2 依赖管理 (`pom.xml`)
*   更新 `maven-compiler-plugin` 目标版本为 `21`。
*   引入 `htmlunit-driver` 依赖（Selenium 4 已将其解耦）。
*   显式添加 `org.json` 依赖。

## 4. 验证与测试
*   **编译验证**: `mvn clean compile` 成功通过。
*   **单元测试**: `mvn test` 运行正常。
    *   *已知限制*: 由于测试目标站点使用现代前端框架，`HtmlUnitDriver` 可能无法完全模拟浏览器行为，建议在 CI/CD 环境中配置 Chrome/Firefox Headless 模式进行完整集成测试。
*   **数据驱动验证**: 验证 Excel/CSV 读取逻辑在 POI 5.x 下正常工作。

## 5. 回滚策略
*   利用 Git 分支管理，若升级失败可快速回退至 `master` 分支。
*   保留旧版 `pom.xml` 备份（通过版本控制）。
