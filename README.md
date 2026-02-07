# LLM 自动代码评审系统

> 基于大语言模型（ChatGLM）的自动化代码评审工具，集成 GitHub Actions 实现 CI/CD 自动化流程

**作者**: zzb

---

## 📖 项目简介

LLM 自动代码评审系统是一个智能化的代码质量管理工具，当开发者提交代码到 GitHub 时，系统会自动：
1. 获取代码变更（git diff）
2. 调用 ChatGLM API 进行智能评审
3. 将评审结果存储到独立的日志仓库
4. 通过微信模板消息推送通知

---

## ✨ 核心特性

- 🤖 **AI 驱动评审**：基于智谱 AI 的 ChatGLM-4-Flash 模型，快速准确
- ⚡ **自动化流程**：GitHub Actions 自动触发，无需人工介入
- 📝 **结果可追溯**：评审记录按日期归档到独立 Git 仓库
- 📱 **实时通知**：微信模板消息即时推送评审结果
- 🔒 **安全可靠**：敏感配置通过 GitHub Secrets 管理
- 🎯 **轻量高效**：预编译 JAR 包，执行时间仅需 30 秒

---

## 🏗️ 项目架构

### 模块结构

```
LLM_code_auto_evaulation/
├── openai-code-review-sdk/      # 核心 SDK 模块
│   ├── domain/                  # 领域模型层
│   │   ├── model/              # 数据模型（请求/响应）
│   │   └── service/            # 业务服务层
│   └── infrastructure/          # 基础设施层
│       ├── git/                # Git 操作
│       ├── openai/             # LLM 接口
│       └── weixin/             # 微信通知
├── openai-code-review-test/     # 测试模块
└── .github/workflows/           # GitHub Actions 配置
```

### 技术栈

- **语言**: Java 8
- **构建工具**: Maven 3.x
- **LLM**: ChatGLM API (智谱 AI)
- **Git 操作**: Eclipse JGit 5.13.0
- **JSON 处理**: Alibaba FastJSON 2.0.49
- **认证**: Auth0 JWT 4.2.2
- **缓存**: Google Guava 32.1.3
- **消息推送**: 微信公众平台 API
- **CI/CD**: GitHub Actions

---

## 🚀 快速开始

### 前置要求

- JDK 11+
- Maven 3.6+
- Git
- GitHub 账号
- ChatGLM API Key（[申请地址](https://open.bigmodel.cn/)）
- 微信公众号测试号（[申请地址](https://mp.weixin.qq.com/debug/cgi-bin/sandbox?t=sandbox/login)）

### 本地构建

```bash
# 克隆项目
git clone https://github.com/namezzb/LLM_code_auto_evaulation.git
cd LLM_code_auto_evaulation

# 编译打包
mvn clean package

# 生成的 JAR 包位置
# openai-code-review-sdk/target/openai-code-review-sdk-2.0.jar
```

### 本地运行

```bash
# 设置环境变量
export GITHUB_REVIEW_LOG_URI="https://github.com/your-username/code-review-log"
export GITHUB_TOKEN="your_github_token"
export COMMIT_PROJECT="your_project_name"
export COMMIT_BRANCH="main"
export COMMIT_AUTHOR="Your Name <your@email.com>"
export COMMIT_MESSAGE="test commit"
export WEIXIN_APPID="your_weixin_appid"
export WEIXIN_SECRET="your_weixin_secret"
export WEIXIN_TOUSER="your_weixin_openid"
export WEIXIN_TEMPLATE_ID="your_template_id"
export CHATGLM_APIHOST="https://open.bigmodel.cn/api/paas/v4/chat/completions"
export CHATGLM_APIKEYSECRET="your_api_key.your_api_secret"

# 运行
java -jar openai-code-review-sdk/target/openai-code-review-sdk-2.0.jar
```

---

## ⚙️ GitHub Actions 配置

### 1. 配置 Secrets

在 GitHub 仓库的 `Settings` → `Secrets and variables` → `Actions` 中添加以下 Secrets：

| Secret 名称 | 说明 | 示例 |
|------------|------|------|
| `CODE_REVIEW_LOG_URI` | 评审日志仓库地址 | `https://github.com/username/log-repo` |
| `CODE_TOKEN` | GitHub Personal Access Token | `ghp_xxxxxxxxxxxx` |
| `WEIXIN_APPID` | 微信公众号 AppID | `wx5a228ff69e28a91f` |
| `WEIXIN_SECRET` | 微信公众号 AppSecret | `0bea03aa1310bac050aae79dd8703928` |
| `WEIXIN_TOUSER` | 接收者 OpenID | `or0Ab6ivwmypESVp_bYuk92T6SvU` |
| `WEIXIN_TEMPLATE_ID` | 模板消息 ID | `l2HTkntHB71R4NQTW77UkcqvSOIFqE_bss1DAVQSybc` |
| `CHATGLM_APIHOST` | ChatGLM API 地址 | `https://open.bigmodel.cn/api/paas/v4/chat/completions` |
| `CHATGLM_APIKEYSECRET` | ChatGLM API Key | `your_key.your_secret` |

### 2. 获取 GitHub Token

访问 [GitHub Settings - Tokens](https://github.com/settings/tokens)，创建 Personal Access Token，需要以下权限：
- `repo` (完整仓库访问权限)
- `workflow` (更新 GitHub Actions 工作流)

### 3. 获取微信配置

访问 [微信公众平台测试号](https://mp.weixin.qq.com/debug/cgi-bin/sandbox?t=sandbox/login)：
1. 获取 `appID` 和 `appsecret`
2. 扫码关注测试号，获取 `微信号`（OpenID）
3. 配置模板消息，获取 `模板ID`

**模板消息格式**：
```
项目：{{repo_name.DATA}}
分支：{{branch_name.DATA}}
作者：{{commit_author.DATA}}
说明：{{commit_message.DATA}}
详情：{{review_log_url.DATA}}
```

### 4. 获取 ChatGLM API Key

访问 [智谱 AI 开放平台](https://open.bigmodel.cn/)：
1. 注册并登录
2. 进入 [API Keys 管理](https://open.bigmodel.cn/usercenter/apikeys)
3. 创建 API Key，格式为 `key.secret`

---

## 🔄 工作流程

### GitHub Actions 执行流程

```
代码提交到 inti-project 分支
    ↓
触发 GitHub Actions Workflow
    ↓
检出代码（fetch-depth: 2）
    ↓
安装 JDK 11
    ↓
下载预编译的 JAR 包（从 GitHub Release）
    ↓
提取 Git 元数据（仓库名、分支、作者、提交信息）
    ↓
运行代码评审 JAR
    ↓
    ├─ 执行 git diff HEAD~1 HEAD
    ├─ 调用 ChatGLM API 评审
    ├─ 提交评审结果到日志仓库
    └─ 发送微信通知
    ↓
完成
```

### 代码评审流程

```java
// 1. 获取代码差异
String diffCode = getDiffCode();

// 2. LLM 评审
String recommend = codeReview(diffCode);

// 3. 记录评审结果
String logUrl = recordCodeReview(recommend);

// 4. 推送消息通知
pushMessage(logUrl);
```

---

## 📂 核心代码说明

### 入口类

**位置**: `openai-code-review-sdk/src/main/java/plus/gaga/middleware/sdk/OpenAiCodeReview.java`

```java
public static void main(String[] args) throws Exception {
    // 初始化 Git 命令
    GitCommand gitCommand = new GitCommand(
        getEnv("GITHUB_REVIEW_LOG_URI"),
        getEnv("GITHUB_TOKEN"),
        getEnv("COMMIT_PROJECT"),
        getEnv("COMMIT_BRANCH"),
        getEnv("COMMIT_AUTHOR"),
        getEnv("COMMIT_MESSAGE")
    );

    // 初始化微信通知
    WeiXin weiXin = new WeiXin(
        getEnv("WEIXIN_APPID"),
        getEnv("WEIXIN_SECRET"),
        getEnv("WEIXIN_TOUSER"),
        getEnv("WEIXIN_TEMPLATE_ID")
    );

    // 初始化 LLM 接口
    IOpenAI openAI = new ChatGLM(
        getEnv("CHATGLM_APIHOST"),
        getEnv("CHATGLM_APIKEYSECRET")
    );

    // 执行评审
    OpenAiCodeReviewService service = new OpenAiCodeReviewService(
        gitCommand, openAI, weiXin
    );
    service.exec();
}
```

### 抽象服务层

**位置**: `openai-code-review-sdk/src/main/java/plus/gaga/middleware/sdk/domain/service/AbstractOpenAiCodeReviewService.java`

采用**模板方法模式**，定义评审流程：

```java
@Override
public void exec() {
    try {
        // 1. 获取提交代码
        String diffCode = getDiffCode();

        // 2. 开始评审代码
        String recommend = codeReview(diffCode);

        // 3. 记录评审结果；返回日志地址
        String logUrl = recordCodeReview(recommend);

        // 4. 发送消息通知；日志地址、通知的内容
        pushMessage(logUrl);
    } catch (Exception e) {
        logger.error("openai-code-review error", e);
    }
}
```

---

## 🎯 设计模式应用

| 设计模式 | 应用位置 | 说明 |
|---------|---------|------|
| **模板方法模式** | `AbstractOpenAiCodeReviewService` | 定义评审流程骨架，子类实现具体步骤 |
| **策略模式** | `IOpenAI` 接口 | 支持多种 LLM 提供商（ChatGLM、OpenAI 等） |
| **依赖注入** | 构造函数注入 | 降低耦合，便于测试和扩展 |
| **工厂模式** | 对象创建 | 统一管理对象创建逻辑 |

---

## 📊 性能优化

### 1. Token 缓存

使用 Guava Cache 缓存 JWT Token，有效期 29 分钟（比实际过期时间提前 1 分钟）：

```java
public static Cache<String, String> cache = CacheBuilder.newBuilder()
    .expireAfterWrite(expireMillis - (60 * 1000L), TimeUnit.MILLISECONDS)
    .build();
```

### 2. 预编译 JAR

GitHub Actions 直接下载预编译的 JAR 包，避免每次构建：
- 构建模式：3-5 分钟
- 下载模式：30-40 秒

### 3. 模型选择

使用 `glm-4-flash` 模型：
- 响应速度：< 1 秒
- 成本：约为 GLM-4 的 1/10
- 适合代码评审场景

---

## 🔧 故障排查

### 常见问题

#### 1. GitHub Actions 失败

**问题**: `git diff HEAD~1 HEAD` 失败

**解决方案**: 确保 workflow 中设置 `fetch-depth: 2`

```yaml
- name: Checkout repository
  uses: actions/checkout@v2
  with:
    fetch-depth: 2  # 必须设置为 2
```

#### 2. ChatGLM API 调用失败

**问题**: 401 Unauthorized

**解决方案**:
- 检查 API Key 格式是否正确（`key.secret`）
- 确认 API Key 是否有效
- 检查 Token 生成逻辑

#### 3. 微信消息推送失败

**问题**: access_token 获取失败

**解决方案**:
- 检查 AppID 和 AppSecret 是否正确
- 确认测试号是否已关注
- 检查模板 ID 是否正确

#### 4. 日志仓库提交失败

**问题**: Permission denied

**解决方案**:
- 确认 GitHub Token 权限包含 `repo`
- 检查日志仓库地址是否正确
- 确认 Token 未过期

---

## 📈 项目版本

### v2.0 (当前版本)

- ✅ 完整的分层架构设计
- ✅ 支持 ChatGLM API 集成
- ✅ 微信模板消息推送
- ✅ GitHub Actions 自动化
- ✅ 评审日志 Git 存储
- ✅ JWT Token 缓存优化

### v1.0 (简化版)

- ✅ 基础代码评审功能
- ✅ 原生 HttpURLConnection 实现
- ✅ 简化的单文件架构

---

## 🤝 贡献指南

欢迎提交 Issue 和 Pull Request！

### 开发流程

1. Fork 本仓库
2. 创建特性分支 (`git checkout -b feature/AmazingFeature`)
3. 提交更改 (`git commit -m 'Add some AmazingFeature'`)
4. 推送到分支 (`git push origin feature/AmazingFeature`)
5. 提交 Pull Request

### 代码规范

- 遵循 Java 编码规范
- 添加必要的注释
- 编写单元测试
- 更新相关文档

---

## 📄 许可证

本项目采用 MIT 许可证 - 详见 [LICENSE](LICENSE) 文件

---

## 👨‍💻 作者

**zzb**

- GitHub: [@namezzb](https://github.com/namezzb)
- 项目地址: [LLM_code_auto_evaulation](https://github.com/namezzb/LLM_code_auto_evaulation)

---

## 🙏 致谢

- [智谱 AI](https://open.bigmodel.cn/) - 提供 ChatGLM API
- [GitHub Actions](https://github.com/features/actions) - CI/CD 平台
- [微信公众平台](https://mp.weixin.qq.com/) - 消息推送服务

---

## 📞 联系方式

如有问题或建议，欢迎通过以下方式联系：

- 提交 [Issue](https://github.com/namezzb/LLM_code_auto_evaulation/issues)
- 发送邮件至项目维护者

---

**⭐ 如果这个项目对你有帮助，请给个 Star 支持一下！**
