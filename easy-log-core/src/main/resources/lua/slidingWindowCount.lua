local res = {}

for i, v in ipairs(KEYS) do
    res[v] = redis.call('zcard', v)
end
return res..''