[![](https://jitpack.io/v/chenxyu/retrofit-adapter-flow.svg)](https://jitpack.io/#chenxyu/retrofit-adapter-flow)

`Retrofit` 返回 `Flow<out T>`

# 依赖版本
| Dependency | Version |
|--|--|
| kotlin | 1.5.21 |
| kotlinx-coroutines-android | 1.5.1 |
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
	implementation 'com.github.chenxyu:retrofit-adapter-flow:1.2.0'
}
```

# 使用方法

```kotlin
@POST(API.LOGIN)
fun login(@Body any: Any): Flow<User>

Retrofit.Builder()
    ...
    .addCallAdapterFactory(FlowCallAdapterFactory.create())
    .build()

lifecycleScope.launch(Dispatchers.Main) {
    githubService.search("kotlinx.coroutines")
        .flowOn(Dispatchers.IO)
        .catch { e ->
            if (e is CancellationException) {
                Toast.makeText(this@MainActivity, e.message, Toast.LENGTH_LONG).show()
            }
        }.collect {
            textView.text = it
        }
}
```
