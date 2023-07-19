local prefix = KEYS[1];
local redisKeys = redis.call('keys', prefix .. '*');

local res = '';

if (not redisKeys)
then
    return res;
end ;

for i, v in ipairs(redisKeys) do
    local count = redis.call('zcard', v)
    res = res .. ',' .. v .. '#' .. count
end

return string.sub(res, 2)