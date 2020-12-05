[![](https://jitpack.io/v/chenxyu/retrofit-adapter-flow.svg)](https://jitpack.io/#chenxyu/retrofit-adapter-flow)

`Retrofit` 返回 `Flow<out T>`

# 依赖版本
| Dependency | Version |
|--|--|
| kotlin | 1.4.20 |
| kotlinx-coroutines-android | 1.4.2 |
| retrofit | 2.9.0 |

# Gradle 依赖

1.root build.gradle

```kotlin
allprojects {
	repositories {
		...
		maven { url "https://jitpack.io" }
	}
}
```

2.app build.gradle

```kotlin
dependencies {
	implementation 'com.github.chenxyu:retrofit-adapter-flow:1.1.2'
}
```

# 使用方法

```kotlin
@POST(API.LOGIN)
fun login(@Body any: Any): Flow<User>

Retrofit.Builder()
    ...
    .addCallAdapterFactory(FlowCallAdapterFactory())
    .build()

lifecycleScope.launch(Dispatchers.Main) {
            var resultFlow: Flow<String>? = null
            withContext(Dispatchers.IO) {
                resultFlow = githubService.search("kotlinx.coroutines")
            }
            resultFlow?.catch { e ->
                // 异常处理
                if (e is CancellationException) {
                    Toast.makeText(this@MainActivity, e.message, Toast.LENGTH_LONG).show()
                }
            }?.collect {
                textView.text = it
            }
        }
```
