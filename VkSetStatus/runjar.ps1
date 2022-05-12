$dir = (Get-Location).path

java -cp "$Env:CLASSPATH;%dir\VkSetStatus.jar" myproject.vkstatus.VkSetStatus