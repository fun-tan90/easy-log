```shell
# 追加第二个远程仓库地址，可以不用多次push
git remote set-url --add origin git@github.com:easy-log/easy-log.git
```

```json
{
  "loggerName":"fun.tan90",
  "logLevel":"DEBUG",
  "cmdType": "SET_LOGGER_LEVEL_CONFIG"
}
```
```json
{
  "cmdType": "GET_LOGGER_CONFIGURATIONS"
}
```

**代码提交规范**
- feat: 新功能
- fix: 修复 Bug
- docs: 文档修改
- perf: 性能优化
- revert: 版本回退
- ci: CICD集成相关
- test: 添加测试代码
- refactor: 代码重构
- build: 影响项目构建或依赖修改
- style: 不影响程序逻辑的代码修改
- chore: 不属于以上类型的其他类型(日常事务)