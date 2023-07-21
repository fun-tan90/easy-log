--获取KEY
local key = KEYS[1];

--获取ARGV内的参数
-- 缓存时间
local expire = tonumber(ARGV[1]);

-- 当前时间
local currentMs = tonumber(ARGV[2]);

-- 唯一值
local uniqueVal = ARGV[3];

--窗口开始时间
local windowStartMs = currentMs - expire * 1000;

--获取key的次数
local current = redis.call('zcount', key, windowStartMs, currentMs);

-- 清除上一个窗口的数据
redis.call("ZREMRANGEBYSCORE", key, 0, windowStartMs);

-- 添加当前成员
redis.call("zadd", key, tostring(currentMs), uniqueVal);
redis.call("expire", key, expire);

--返回key的次数
return current .. '#' .. windowStartMs .. '#' .. currentMs