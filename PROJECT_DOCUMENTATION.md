 # ti_live_app 项目完整文档
 
 ## 1. 项目概述
 
 **ti_live_app** 是一个基于微服务架构的直播平台，提供用户登录、即时通讯（IM/WebSocket聊天）、直播推拉流、点播视频管理及分布式ID生成等核心能力。
 
 ### 1.1 技术栈
 
 | 类别 | 技术 | 版本 |
 | --- | --- | --- |
 | **语言** | Java | 17 |
 | **基础框架** | Spring Boot | 3.1.6 |
 | **微服务** | Spring Cloud | 2022.0.4 |
 | **服务治理** | Spring Cloud Alibaba | 2022.0.0.0 |
 | **RPC 框架** | Apache Dubbo | 3.2.11 |
 | **注册中心/配置中心** | Nacos | 2.2.4 |
 | **持久化** | MyBatis-Plus | 3.5.7 |
 | **数据库** | MySQL | 8.0.33 |
 | **缓存** | Redis + Lettuce | - |
 | **分布式 ID** | CosId | 1.20.5 |
 | **短信服务** | 容联云 Cloopen SDK | 1.0.4 |
 | **构建工具** | Maven | - |
 
 ### 1.2 端口分配
 
 | 模块 | 端口 | 说明 |
 | --- | --- | --- |
 | ti-live-gateway | 9000 | Spring Cloud Gateway 网关 |
 | ti-live-api | 8080 | BFF 聚合层 |
 | ti-live-user-provider | 8081 | 用户服务 |
 | ti-live-im-provider | 8085 | IM/WebSocket 服务 |
 | ti-live-live-provider | 8086 | 直播流服务 |
 | ti-live-vod-provider | 8087 | 点播视频服务 |
 | id-generate-provider | 8090 | 分布式 ID 生成 |
 | Nacos | 8848 | 注册配置中心 |
 
 ### 1.3 数据库
 
 | 数据库名 | 用途 | 所属模块 |
 | --- | --- | --- |
 | ti_live_db | 用户表(user, user_phone, sms) | ti-live-user |
 | im_live | 即时消息(im_message) | ti-live-im |
 | ti_live | 直播流(live_stream)、点播视频(vod_video) | ti-live-live, ti-live-vod |
 | id_generator | CosId 机器号和号段表 | id-generate |
 
 ---
 
 ## 2. 架构设计
 
 ### 2.1 系统架构图
 
 ```
 [前端客户端/浏览器]
         |
         v
 [ti-live-gateway :9000]  -- Spring Cloud Gateway
   +-- 全局认证过滤器 (AuthorizationFilter)
   +-- 路由: /api/** -> ti-live-api
   +-- 路由: /id/**  -> id-generate-provider
   +-- 路由: /ws/**  -> ti-live-im-provider (WebSocket)
         |
         v
 [ti-live-api :8080]  -- BFF 聚合层 (Dubbo Consumer)
   +-- Dubbo RPC -> ti-live-user-provider :8081
   +-- Dubbo RPC -> ti-live-im-provider :8085
   +-- Dubbo RPC -> id-generate-provider :8090
 
 [各业务 Provider 微服务]
   +-- ti-live-user-provider :8081  -- 用户服务 + 短信 + Token
   +-- ti-live-im-provider  :8085  -- IM/WebSocket 聊天
   +-- ti-live-live-provider :8086  -- 直播流管理
   +-- ti-live-vod-provider  :8087  -- 点播视频管理
   +-- id-generate-provider  :8090  -- 分布式 ID 生成
 ```
 
 ### 2.2 通信方式
 
 - **Dubbo RPC**: 服务间同步调用，注册中心用 Nacos
 - **Spring Cloud Gateway**: 前端请求统一由网关转发的 HTTP 路由
 - **WebSocket**: IM 模块的实时聊天通过原生 WebSocket
 - **Redis**: 短信验证码缓存、Token 存储、在线人数统计
 - **Nacos**: 服务注册与发现、配置中心
 
 ### 2.3 模块依赖关系
 
 ```
 ti-live-common (工具类、枚举)
    |
    +--- ti-live-starter (自动配置: Redis)
    |
    +--- ti-live-user-interface (RPC接口: IUserRPCService, IUserMobileRPCService)
    |       |
    |       +--- ti-live-user-provider (用户服务实现)
    |
    +--- ti-live-im-interface (RPC接口: IImMessageService, IImRoomService)
    |       |
    |       +--- ti-live-im-provider (IM服务实现 + WebSocket)
    |
    +--- ti-live-live-interface (RPC接口: ILiveStreamService)
    |       |
    |       +--- ti-live-live-provider (直播流服务实现)
    |
    +--- ti-live-vod-interface (RPC接口: IVodVideoService)
    |       |
    |       +--- ti-live-vod-provider (点播服务实现)
    |
    +--- id-generate-interface (RPC接口: IIdGenerateService)
    |       |
    |       +--- id-generate-provider (CosId + 线程池实现)
    |
    +--- ti-live-api (BFF, 依赖所有 interface 模块)
    |
    +--- ti-live-gateway (网关, 依赖 user-interface 做 Token 校验)
    |
    +--- ti-live-order (订单模块, 占位)
    +--- ti-live-room (房间模块, 占位)
    +--- ti-live-gift (礼物模块, 占位)
 ```
 
 ### 2.4 认证流程
 
 ```
 1. 用户请求 /api/user/sendSMSCode 或登录(write-list绕过认证)
 2. 用户输入验证码 -> 调用 /api/user/mobileLogin
 3. UserRPCService.createAndSaveLoginToken() 生成 UUID token 存入 Redis
 4. 返回 token 并写入 Cookie tltk (有效期7天)
 5. 后续请求通过网关 AuthorizationFilter
 6. 网关解析 Cookie tltk, 调用 UserRPCService.getUserIdByToken() 验证
 7. 验证通过 -> 在 Header 添加 X-User-Id -> 转发给下游服务
 8. 验证失败 -> 返回 401
 ```
 
 ---
 
 ## 3. 模块详解
 
 ### 3.1 ti-live-common (公共组件)
 
 路径: `ti-live-common/`\
 说明: 所有微服务共享的枚举类和工具类。
 
 #### 枚举
 
 | 枚举 | 值 | 用途 |
 | --- | --- | --- |
 | `ImMsgTypeEnum` | TEXT(1), IMAGE(2), GIFT(3), SYSTEM(4), ENTER_ROOM(5), LEAVE_ROOM(6) | IM消息类型 |
 | `LiveStatusEnum` | PREPARING(0), LIVING(1), ENDED(2), CANCELED(3) | 直播状态 |
 | `OrderStatusEnum` | UNPAID(0), PAID(1), COMPLETED(2), CANCELED(3), REFUNDED(4) | 订单状态 |
 | `ResultCodeEnum` | SUCCESS(200), FAIL(500), UNAUTHORIZED(401) ... | 统一返回码 |
 | `SmsSceneEnum` | LOGIN(1), REGISTER(2), FORGET_PASSWORD(3), BIND_PHONE(4) | 短信场景 |
 | `UserStatusEnum` | DISABLED(0), NORMAL(1) | 用户状态 |
 | `YesNoEnum` | NO(0), YES(1) | 通用是非 |
 
 #### 工具类
 
 - **ConvertBeanUtil**: 封装 Spring BeanUtils.copyProperties，支持类型安全的 POJO 转换
 - **EncryptUtil**: 加密工具类
   - `aesEncrypt/Decrypt` - AES-GCM 加解密
   - `md5` - MD5 散列
   - `sha256/sha256WithSalt` - SHA-256 散列（支持加盐）
   - `generateAESKey` - 生成 AES 密钥
 
 ### 3.2 ti-live-starter (自动配置)
 
 路径: `ti-live-starter/`\
 说明: Redis 自动配置模块，提供给其他微服务使用。
 
 - **RedisConfig**: 配置 `RedisTemplate<String, Object>`，序列化使用 StringRedisSerializer
 - **RedisKeyBuilder**: 基类，提供 `{应用名}:` 前缀的 Redis key 构建
 - **SMSCacheKeyBuilder**: 继承 RedisKeyBuilder，专门构建 SMS 验证码缓存 key
 - **RedisProperties**: `@ConfigurationProperties(prefix = "ti.redis")`，提供 host/port/password/pool 配置
 
 ### 3.3 ti-live-gateway (网关)
 
 路径: `ti-live-gateway/`\
 端口: 9000\
 角色: 所有外部请求的入口，负责路由转发和 Token 鉴权。
 
 **核心组件**:
 
 - **GatewayApplication**: 启动类，标注 `@EnableDiscoveryClient` + `@EnableDubbo`
 - **AuthorizationFilter**: 全局过滤器（Order=-100）
   - 白名单配置: 从 `tlivegateway.white-list` 读取无需认证路径
   - 白名单: `/api/user/login`, `/api/user/mobileLogin`, `/api/user/sendSMSCode`, `/api/id/**`, `/actuator/**`, `/health`
   - 非白名单路径: 从 Cookie `tltk` 取 token，调用 Dubbo `IUserRPCService.getUserIdByToken()` 验证
   - 验证成功: 在 Header 添加 `X-User-Id` 后转发给下游
 - **GatewayAppProperities**: 白名单配置属性类
 
 **路由规则**:
 
 | 路由ID | 匹配路径 | 目标服务 |
 | --- | --- | --- |
 | ti-live-api | /api/** | lb://ti-live-api |
 | id-generate-provider | /id/** | lb://id-generate-provider |
 | ti-live-im-ws | /ws/** | ws://localhost:8085 |
 
 ### 3.4 ti-live-api (BFF 聚合层)
 
 路径: `ti-live-api/`\
 端口: 8080\
 角色: 前端 API 入口，聚合多个 Dubbo 服务，提供统一的 REST 接口。
 
 **Application.java**: 
 - 标注 `@EnableDubbo`, `@EnableDiscoveryClient`
 - 使用 Nacos 作为服务发现，Nacos 地址: `127.0.0.1:8848`
 - Dubbo 注册方式: `address: N/A` (直连模式 consumer，通过 Nacos discovery 发现 provider)
 
 **Controller 层**:
 
 #### UserController (`/user`)
 
 | 接口 | 方法 | 说明 |
 | --- | --- | --- |
 | `/user/getUserById` | GET | 从 Header `X-User-Id` 获取用户ID，调用 `IUserRPCService.getUserById()` |
 | `/user/sendSMSCode` | POST | 发送登录短信验证码，调用 `IUserRPCService.sendLoginCode()` |
 | `/user/mobileLogin` | POST | 手机验证码登录，校验验证码后调用 `IUserMobileRPCService.login()`，生成 Token 写入 Cookie |
 
 #### ImController (`/api/im`)
 
 | 接口 | 方法 | 说明 |
 | --- | --- | --- |
 | `/api/im/room/create` | POST | 创建聊天室 |
 | `/api/im/room/list` | GET | 获取在线房间列表 |
 | `/api/im/room/info` | GET | 获取房间信息 |
 | `/api/im/room/onlineCount` | GET | 获取房间在线人数 |
 | `/api/im/room/close` | POST | 关闭房间 |
 | `/api/im/message/history` | GET | 获取房间历史消息 |
 | `/api/im/message/latest` | GET | 获取最新消息 |
 | `/api/im/message/read` | POST | 标记消息已读 |
 
 #### ImServiceController (`/api/im/service`)
 
 通过 Redis 实现 IM 服务实例的注册、注销、查询和心跳。
 
 | 接口 | 方法 | 说明 |
 | --- | --- | --- |
 | `/api/im/service/register` | POST | 注册 IM 服务实例到 Redis |
 | `/api/im/service/unregister` | POST | 注销 IM 服务实例 |
 | `/api/im/service/list` | GET | 获取所有注册的 IM 服务 |
 | `/api/im/service/wsUrl` | GET | 获取指定 IM 服务的 WebSocket 地址 |
 | `/api/im/service/heartbeat` | POST | 发送心跳 |
 
 设计: `im:service:registry`(Set) + `im:service:registry:{name}`(Hash)
 
 #### IdGenerateController (`/api/id`)
 
 | 接口 | 方法 | 说明 |
 | --- | --- | --- |
 | `/api/id/generate` | POST | 生成单个 ID（支持 String/Long 格式） |
 | `/api/id/batch` | POST | 批量生成 ID（最大 1000 个） |
 | `/api/id/generate/{businessName}` | GET | 生成字符串格式 ID |
 | `/api/id/generate/long/{businessName}` | GET | 生成 Long 格式 ID |
 
 **Entity 层**:
 - **WebResDTO**: 统一响应体，code=200/500，静态工厂方法 success()/fail()/error()
 - **MobileLoginParam**: 手机号 + 验证码
 - **IdGenerateParam**: businessName, stringFormat, count
 - **ImRegisterParam**: imServiceName, wsUrl, wsPort
 
 **配置**: `bootstrap.yml` 从 Nacos 拉取 `common.yaml` 和 `ti-live-api-ext.yaml`
 
 ### 3.5 ti-live-user (用户服务)
 
 路径: `ti-live-user/`\
 端口: 8081\
 分为 interface (RPC 接口定义) 和 provider (实现)。
 
 #### Interface 层
 
 **IUserRPCService**: Dubbo 接口
 - `getUserById(Long)`: 根据用户ID获取用户信息
 - `sendLoginCode(String)`: 发送登录验证码
 - `checkLoginCode(String, int)`: 校验验证码
 - `createAndSaveLoginToken(Long)`: 创建并保存登录 Token
 - `getUserIdByToken(String)`: 根据 Token 获取用户ID
 
 **IUserMobileRPCService**: Dubbo 接口
 - `login(String)`: 手机号一键登录/注册
 
 **DTO**: UserDTO, userLoginDTO, MsgCheckDTO
 
 #### Provider 层
 
 **UserRPCService**: `@DubboService`
 - getUserById: UserMapper -> ConvertBeanUtil 转换
 - sendLoginCode / checkLoginCode: 委托 SmsService
 - createAndSaveLoginToken / getUserIdByToken: 委托 TokenService
 
 **UserMobileRPCService**: `@DubboService` 手机登录
 - 按 phone 查 user -> 不存在自动注册 -> 生成 Token -> 返回 userId
 
 **TokenService**: UUID token -> Redis `user:token:{token}` -> userId, 7天过期
 
 **SmsService**: 
 - 生成6位随机验证码 -> 写入 t_sms 表 + Redis (5分钟过期)
 - checkLoginCode: 从 Redis 取并比对
 
 **UserService**: 传统的 Service 层（非 Dubbo 暴露）, 部分功能与 RPCService 重叠
 
 **实体类**:
 - **User**: 继承 UserDO，@TableName("user")，简化字段集
 - **UserDO**: @TableName("t_user")，完整23字段含 userType(1普通/2VIP/3管理员), registerIp 等，业务方法 isEnabled(), isVip(), getMaskedPhone()
 - **SmsDO**: @TableName("t_sms"), phone, code, sceneType, status, expireTime
 
 **Mapper**: UserMapper, SmsMapper 继承 BaseMapper
 
 **配置类**:
 - MyBatisPlusConfig: 分页插件
 - MyMetaObjectHandler: 自动填充 createTime/updateTime/deleted
 - RedisConfig: RedisTemplate 配置
 - CloopenSmsConfig: 容联云 SDK Bean, serverIp=app.cloopen.com, port=8883
 - DubboClientConfig / EnableDubboClient: 自定义组合注解
 
 ### 3.6 ti-live-im (IM 即时通讯服务)
 
 路径: `ti-live-im/`\
 端口: 8085\
 角色: 提供聊天室管理和 WebSocket 实时通信。
 
 **IImMessageService**: 
 - sendMessage / batchSendMessages: 发送消息
 - getRoomHistory / getLatestMessages: 获取消息（SQL LIMIT）
 - markAsRead: 标记已读
 
 **IImRoomService**: 
 - createRoom / closeRoom: 房间生命周期
 - enterRoom / leaveRoom: Redis Set 在线管理
 - getRoomInfo / getOnlineRooms / getOnlineCount
 
 **DTO**: ImMessageDTO, ImRoomDTO, ImGiftDTO
 
 **ImRoomServiceImpl**:
 - ConcurrentHashMap 本地缓存 + Redis 辅助
 - 在线人数: Redis Set `im:online:{roomId}`
 - 房间 ID: AtomicLong 本地自增
 
 **ImMessageServiceImpl**:
 - 消息持久化 MySQL `im_message`
 - giftInfo JSON 序列化, isBarrage 0/1 转换
 
 **WebSocket 模块 (4个核心类)**:
 
 - **ChatWebSocketHandler**: TextWebSocketHandler
   - 连接建立: 添加 session, 系统消息, 发送历史50条
   - 消息处理: 解析 JSON -> 文本/礼物 -> 保存 -> broadcast
   - 连接关闭: 移除 session, leaveRoom
 
 - **WebSocketHandshakeInterceptor**: 握手拦截
   - 从 URL Query 解析 token/userId/roomId
   - 通过 WebSocketAuthValidator 校验
 
 - **WebSocketAuthValidator**: Dubbo 调用 IUserRPCService 校验 token
 
 - **ImRoomSessionManager**: 两层 Map 管理 session
   - broadcast(roomId, msg, excludeUserId): 群发
   - sendToUser(userId, msg): 点对点
 
 **WebSocketConfig**: 注册 `/ws/chat`, 跨域 `*`
 
 ### 3.7 ti-live-live (直播流服务)
 
 路径: `ti-live-live/`\
 端口: 8086
 
 **ILiveStreamService** 接口方法:
 - createLiveStream: 创建流, 生成推拉流地址
 - startLiveStream: 状态 -> 1, Redis 缓存
 - endLiveStream: 状态 -> 2
 - getLiveStreamInfo / getRoomLiveStream / getLiveStreams
 - getAnchorLiveHistory / updateViewerCount
 
 推流地址: `rtmp://localhost:1935/live?streamKey={streamKey}`\
 拉流地址: `http://localhost:8080/hls/{streamKey}.m3u8`
 
 ### 3.8 ti-live-vod (点播视频服务)
 
 路径: `ti-live-vod/`\
 端口: 8087
 
 **IVodVideoService** 接口方法:
 - uploadVideo: 上传(状态=0待审核)
 - getVideoInfo: 优先 Redis 缓存
 - updateVideo / deleteVideo / auditVideo
 - incrementPlayCount: Redis 原子递增
 - getVideoList / getUserVideos / searchVideos: 分页 + 模糊搜索
 
 ### 3.9 id-generate (分布式 ID 生成)
 
 路径: `id-generate/`\
 端口: 8090\
 技术: CosId 1.20.5 (Snowflake + Segment)
 
 **IIdGenerateService**: 8种生成方式
 - generateId / batchGenerateId: 通用/批量
 - generateIdAsString / generateIdAsLong: 快捷方法
 - generateSequentialOrderId: 顺序订单号(前缀+时间戳+6位随机)
 - generateRandomOrderId: 随机订单号(前缀+32位随机)
 - generateSnowflakeId / generateSegmentId: 委托 CosId
 - generateDistributedStringId: 分布式字符串
 
 **CosId 配置**: namespace=id-generate, JDBC 机器号分配, Snowflake 配置
 
 **IdGenerateThreadPoolConfig**: 核心=CPU*2, 最大=CPU*4, 队列1万
 
 ### 3.10 占位模块
 
 ti-live-order, ti-live-room, ti-live-gift 仅有 pom.xml 定义，无源代码。
 
 ---
 
 ## 4. 数据模型
 
 ### 4.1 user / t_user (用户表)
 
 | 字段 | 类型 | 说明 |
 | --- | --- | --- |
 | id | BIGINT PK | 自增主键 |
 | username | VARCHAR(50) UK | 用户名 |
 | password | VARCHAR(100) | 密码 |
 | nickname | VARCHAR(50) | 昵称 |
 | avatar | VARCHAR(255) | 头像 URL |
 | email | VARCHAR(100) UK | 邮箱 |
 | phone | VARCHAR(20) UK | 手机号 |
 | gender | TINYINT | 0-未知,1-男,2-女 |
 | status | TINYINT | 0-禁用, 1-启用 |
 | user_type | TINYINT | 1-普通, 2-VIP, 3-管理员 |
 | register_ip | VARCHAR(50) | 注册 IP |
 | last_login_ip | VARCHAR(50) | 最后登录 IP |
 | last_login_time | DATETIME | 最后登录时间 |
 | create_time / update_time | DATETIME | 自动填充 |
 | deleted | TINYINT | 逻辑删除 |
 
 ### 4.2 t_sms (短信记录)
 
 id, phone, code, sceneType, status, expireTime, createTime
 
 ### 4.3 im_message (IM 消息)
 
 msg_id, sender_id, sender_name, room_id, msg_type, content, gift_info(JSON), is_barrage, send_time(时间戳), is_read
 
 ### 4.4 live_stream (直播流)
 
 stream_id, anchor_id, anchor_name, room_id, title, cover_url, push_url, pull_url, status(0/1/2), viewer_count, start_time, end_time
 
 ### 4.5 vod_video (点播视频)
 
 video_id, user_id, title, description, cover_url, video_url, duration, play_count, status(0/1/2), tags
 
 ### 4.6 CosId 表
 
 cosid_machine: ns, biz_tag, version, last_timestamp, worker_id\
 cosid_segment: ns, biz_tag, max_id, step, delta
 
 ---
 
 ## 5. 部署与启动
 
 ### 5.1 前置环境
 
 JDK 17+, MySQL 8.0+, Redis 6+, Nacos 2.2.4+, Maven 3.8+
 
 ### 5.2 启动步骤
 
 1. 创建数据库: 执行各模块 db/schema.sql
 2. 启动 Nacos: standalone 模式
 3. 配置 Nacos namespace `dev`, 配置 common.yaml + ti-live-api-ext.yaml
 4. 修改各模块 application.yml 中的数据库连接和 Redis 配置
 5. `mvn clean package -DskipTests`
 6. 按顺序启动: Nacos -> id-generate -> user-provider -> im-provider -> api -> gateway
 
 ### 5.3 服务启动顺序
 
 Dubbo consumer 配置 `check: false` 所以启动不强制依赖。推荐: Nacos -> id-generate -> user -> im -> live/vod -> api -> gateway
 
 ---
 
 ## 6. Redis Key 设计
 
 | Key Pattern | 类型 | 用途 | 过期 |
 | --- | --- | --- | --- |
 | `user:token:{token}` | String | Token -> userId | 7天 |
 | `sms:login:code:{mobile}` | String | 验证码 | 5分钟 |
 | `im:service:registry` | Set | IM 服务列表 | - |
 | `im:service:registry:{name}` | Hash | IM 服务详情 | - |
 | `im:room:{roomId}` | String | 房间名 | - |
 | `im:online:{roomId}` | Set | 在线用户 | - |
 | `live:stream:{streamId}` | String | 直播状态 | - |
 | `live:viewer:{streamId}` | String | 观看人数 | - |
 | `vod:video:{videoId}` | String | 视频缓存 | 30分钟 |
 | `vod:playCount:{videoId}` | String | 播放计数 | - |
 
 ---
 
 ## 7. 设计决策与关键思考
 
 ### 7.1 两套 user 实体 (User / UserDO)
 - UserDO 映射 `t_user`, 完整 23 字段含业务方法
 - User 继承 UserDO, 映射 `user`, 简化版
 - 推测: 代码经历了重构，旧版 user 表演进到 t_user 表
 
 ### 7.2 Dubbo 注册方式不一致
 - api / gateway: `address: N/A` (混合模式, 通过 Spring Cloud Nacos Discovery 发现)
 - im / live / vod: `address: nacos://...` (标准 Dubbo 注册)
 - id-generate: `address: N/A`
 
 ### 7.3 WebSocket 认证设计
 - 用 URL Query 传递 token/ userId/ roomId
 - 握手拦截器提取参数 -> WebSocketAuthValidator 调 Dubbo 校验
 
 ### 7.4 IM 服务注册发现
 - 独立于 Nacos, 用 Redis 实现轻量服务注册表
 
 ### 7.5 ID 生成策略
 - 顺序订单: 前缀+时间戳+6位随机 (可读)
 - 随机订单: 前缀+32位随机 (不可预测)
 - CosId: Snowflake (性能/趋势递增) + Segment (批量取号)
 
 ### 7.6 容联云集成不完整
 - CloopenSmsConfig 配好了 SDK Bean, 但 SmsService.sendLoginCode 未实际调用 SDK
 
 ---
 
 ## 8. 已知问题与技术债务
 
 | # | 问题 | 建议 |
 | --- | --- | --- |
 | 1 | SMS 仅写库未发送 | 集成真实 Cloopen SDK 调用 |
 | 2 | 类名拼写: GatewayAppProperities | 重命名为 Properties |
 | 3 | UserMobileService.login() 返回 null | 清理或实现 |
 | 4 | 数据库密码硬编码 | 环境变量/Nacos |
 | 5 | CosId machine-id=0 硬编码 | 用 JDBC 自动分配 |
 | 6 | ImRoomServiceImpl 用本地 AtomicLong | 改为 ID 生成服务 |
 | 7 | Nacos 配置依赖 dev namespace | 多环境变量控制 |
 | 8 | order/room/gift 模块无实现 | 按需开发 |
 | 9 | VOD Redis 缓存操作被注释 | 启用反序列化 |
 | 10 | 容联云配置为占位符 | 替换为真实配置 |
 
 ---
 
 ## 9. 关键流程图
 
 ### 9.1 用户手机号登录
 
 ```
 用户输入手机号 -> POST /api/user/sendSMSCode
   -> 生成6位随机码 -> 存 t_sms 表 + Redis (5min)
 
 用户输入手机号+验证码 -> POST /api/user/mobileLogin
   -> checkLoginCode: Redis 校验验证码
   -> login: 查 user 表, 不存在自动注册
   -> 生成 UUID token, 存 Redis 7天
   -> 设置 Cookie tltk
 ```
 
 ### 9.2 WebSocket 聊天
 
 ```
 1. 前端查 IM 地址: GET /api/im/service/list
 2. 建立 ws: ws://host:8085/ws/chat?token=xxx&userId=123&roomId=456
 3. 握手拦截器校验 -> 注入 attributes
 4. 连接建立: 添加 session, 系统通知, 发历史50条
 5. 消息: JSON -> DB -> broadcast
 6. 断开: 移除 session, 系统通知
 ```
 
 ### 9.3 网关认证
 
 ```
 请求 -> AuthorizationFilter
   -> 白名单? 放行
   -> 非白名单: 读 Cookie tltk
      -> 无 Cookie: 401
      -> 有 Token: Dubbo 校验
         -> 无效: 401
         -> 有效: Header 加 X-User-Id -> 转发
 ```
 
 ---
 
 ## 10. 安全设计
 
 - Token 认证: UUID + Redis, 7天过期
 - Cookie: HttpOnly tltk
 - 网关统一认证, 下游信任 X-User-Id
 - 短信验证码 5 分钟过期, Redis 集中校验
 - AES-GCM / MD5 / SHA-256 加密工具
 - MyBatis-Plus 逻辑删除 + 自动填充
 
 ---
 
 *文档生成日期: 2026-07-19 | 基于代码逆向分析生成, 覆盖全部模块和函数。*
