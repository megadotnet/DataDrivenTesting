# 生产环境切换 Checklist (Production Switch Checklist)

## 1. 预检查 (Pre-Check)
- [ ] **构建状态**: 确认 `mvn clean package` 在 Java 21 环境下构建成功且无 `ERROR`。
- [ ] **依赖版本**: 确认 `pom.xml` 中无 `SNAPSHOT` 版本依赖（除内部开发包外）。
- [ ] **环境一致性**: 生产/CI 服务器已安装 JDK 21，并设置为默认 `JAVA_HOME`。

## 2. 部署与配置
- [ ] **驱动配置**: 确认生产环境/CI 节点已安装 Chrome/Firefox 浏览器（如使用 GUI 测试）。
- [ ] **路径配置**: 检查 `test.properties` 中的路径配置是否符合当前操作系统规范。
- [ ] **日志配置**: 确认 `logback.xml` 配置有效，日志输出路径可写。

## 3. 冒烟测试
- [ ] **核心流程**: 运行一次完整的登录测试 (`LoginPageTest`)。
- [ ] **数据读取**: 确认能从 Excel/CSV 正确读取测试数据。
- [ ] **报告生成**: 确认 TestNG 报告生成无误。

## 4. 监控与回滚
- [ ] **监控**: 观察第一次全量回归测试的内存占用 (Java 21 内存模型优化) 和执行耗时。
- [ ] **回滚**: 若出现严重阻塞性问题，立即切换回基于 Java 8 的旧 Docker 镜像或分支。
