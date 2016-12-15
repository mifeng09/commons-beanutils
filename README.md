# commons-beanutils

## purpose

Provide an easy way to update a bean from Map.

## usage
see tests

* construct

use static method ```com.cloume.commons.beanutils.Updater.wrap(R)``` create new instance, *Updater.wrap(```theObjectYouWantToUpdate```)* return the Updater<T> instance

```
class UpdatableClass { ... }
Updater<UpdatableClass> updater = Updater.wrap(new UpdatableClass());
```

* keys mapping

method ```com.cloume.commons.beanutils.Updater.mapping(String, String)``` use to map a key name to another, for example a class has a field named ```someField```, when there is a key named ```anotherField```, can use the ```mapping``` set the value of ```anotherField``` to the ```someField``` of target class instance. and method ```mapping``` return the Updater<T> instance self, so can write multi-mapping in chain

```
Updater<UpdatableClass> updater = updater
	.mapping("anotherField", "someField")
	.mapping("helloField", "hiField"); 
```

* update

there are two overwrited methods ```com.cloume.commons.beanutils.Updater.update(Map<String, Object>)``` and ```com.cloume.commons.beanutils.Updater.update(Map<String, Object>, IConverter)```, use to update the target instance from a Map<String, Object>.

1. update without converter

example:
```
class UpdatableClass {
	String someField;
	int hiField;
}

Map<String, Object> from = new HashMap<String, Object>();
from.put("someField", "hello,world");
from.put("helloField", 100);	///see mapping
UpdatableClass updated = updater.update(from);
```

1. update with converter

converter ```com.cloume.commons.beanutils.IConverter``` have two methods ```com.cloume.commons.beanutils.IConverter.convert(String, Object)``` must be implemented, and ```com.cloume.commons.beanutils.IConverter.pair(String, Object)``` provide an easy way create new ```Entry<K,V>``` instance. 
when using converter you can both maping key and modify the value to fit the class field needs.

example:
```
ObjectToUpdate updated = Updater.wrap(new ObjectToUpdate()).mapping("longValue2", "longValue").update(from,
	/// java8 can use lambda
	new IConverter() {
		@SuppressWarnings("unchecked")
		@Override
		public Entry<String, Object> convert(String key, Object value) {
			switch (key) {
			case "intValue": {
				return pair(key, Integer.parseInt(String.valueOf(value)));
			}
			case "arrayValue": {
				return pair(key, StringUtils.split(String.valueOf(value), ','));
			}
			case "stringValue2": {
				return pair("stringValue", value);
			}
			case "classValue": {
				return pair(key, Updater.wrap(new InnerClass()).update((Map<String, Object>) value));
			}
			}
			return pair(key, value);		///pass through
		}
	});
```

* complex field

example:
```
class InnerClass { String innerField; }
class UpdatableClass {
	String someField;
	int hiField;
	InnerClass classField;
}

Map<String, Object> from = new HashMap<String, Object>();
from.put("classField.innerField", "hello,world");
UpdatableClass updated = updater.update(from);
```

