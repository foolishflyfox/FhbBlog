
- 查看 os 信息
    - 查看操作系统版本：`cat /etc/os-release`
    - 查看内核信息 `uname -a` / `uname -v`
- 查看路由表: `netstat -rn`

- Linux 配置(huabinfeng)
    - Ubuntu
        - 设置默认命令行模式：sudo systemctl set-default multi-user.target 
    - 设置主机名: `hostnamectl set-hostname "xxx"`
    - ssh
        - 密码登录: `ssh-copy-id -i ~/.ssh/id_rsa.pub 用户名@主机名或IP`
