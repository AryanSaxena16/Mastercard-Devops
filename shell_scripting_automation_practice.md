# Shell Scripting & Automation — Scenario-Based Practice

## How to use this document

This is a **practice-only workbook**.

- For a **Direct Question**, answer directly with the command/syntax.
- For a **Scenario**, write the commands you would actually run.
- If you don't know the exact command, write the **steps you would take** instead.
- Do not look up the answer immediately. Try to reason first.
- Partial answers are completely fine.

### How your answers will be reviewed

For every answer, review will focus on:

1. Correctness
2. Understanding of Linux/Bash
3. Troubleshooting flow
4. Production safety
5. Automation thinking
6. Efficiency
7. Quality of reasoning

---

# Part 1 — Direct Questions

## Q1 — Shell script

What is a shell script?

What problem does it solve compared with manually running commands one by one?

---

## Q2 — Shebang

What does this line mean?

```bash
#!/bin/bash
```

---

## Q3 — Create and run a script

Write the commands to:

1. Create `server-check.sh`
2. Make it executable
3. Run it

---

## Q4 — echo

What does this do?

```bash
echo "Server is healthy"
```

---

## Q5 — Variables

Create a variable called `APP_NAME` containing:

```text
employee-portal
```

Then print its value.

---

## Q6 — Variable syntax

What is wrong with this?

```bash
APP_NAME = "employee-portal"
```

Write the correct syntax.

---

## Q7 — Command substitution

Write a command that stores the output of:

```bash
hostname
```

inside `SERVER_NAME`.

---

## Q8 — Exit status

What does this do?

```bash
echo $?
```

What does `0` normally mean? What does a non-zero value normally mean?

---

## Q9 — File condition

Write a Bash condition that checks whether `/tmp/test.txt` exists and is a regular file.

---

## Q10 — Directory condition

Write a Bash condition that checks whether `/var/log` exists and is a directory.

---

## Q11 — Number comparison

What operator checks:

> Is CPU usage greater than 80?

---

## Q12 — String comparison

Write a condition that checks whether `ENVIRONMENT` is equal to `production`.

---

## Q13 — for loop

Write a loop that prints all `.log` files in the current directory.

---

## Q14 — while loop

What is the difference between a `for` loop and a `while` loop?

Give one DevOps use case for each.

---

## Q15 — sleep

What does this do?

```bash
sleep 10
```

Give one automation scenario where it is useful.

---

## Q16 — Script arguments

If the script is executed as:

```bash
./deploy.sh employee-portal production
```

What are:

```text
$0
$1
$2
$#
$@
```

---

## Q17 — Functions

What is a Bash function?

Why use one instead of repeating commands?

---

## Q18 — Command chaining

Explain the difference between:

```bash
command1 && command2
```

and:

```bash
command1 || command2
```

---

## Q19 — Pipes

What does this do?

```bash
ps aux | grep nginx
```

Explain what `|` does.

---

## Q20 — Output redirection

Explain the difference between:

```bash
command > output.log
```

and:

```bash
command >> output.log
```

---

## Q21 — Error redirection

What does this mean?

```bash
command 2> error.log
```

What does `2` represent?

---

## Q22 — Combined output and errors

What does this do?

```bash
command > output.log 2>&1
```

---

# Part 2 — Basic Automation Scenarios

## Scenario 1 — Server health check

Your manager says:

> "Before starting work on a server, I want one script that gives me a quick health check."

The script should display:

- Hostname
- Uptime
- Memory usage
- Disk usage of `/`
- Top CPU-consuming processes

### Your task

Write the commands/script you would use.

---

## Scenario 2 — Disk warning

A production server is showing signs of running out of disk space.

The script should:

- Check `/`
- Get disk usage percentage
- Print `OK` if below 80%
- Print `WARNING` if 80–89%
- Print `CRITICAL` if 90% or above

### Your task

Write the script.

If you don't remember how to extract the percentage, write the steps you would take.

---

## Scenario 3 — Application status

The application is called:

```text
payment-service
```

If running, print:

```text
payment-service is running
```

Otherwise:

```text
ALERT: payment-service is DOWN
```

### Your task

Write the script.

---

## Scenario 4 — Multiple applications

Check these four services:

```text
nginx
redis
payment-service
employee-portal
```

### Your task

Use a loop instead of four separate blocks.

---

## Scenario 5 — Search logs for errors

You have:

```text
app.log
payment.log
database.log
access.log
```

Check every `.log` file for:

```text
ERROR
```

### Your task

Use a loop and `grep`.

---

## Scenario 6 — Count errors

The application team asks:

> "How many ERROR entries are currently in `app.log`?"

### Your task

Write a command or script that gives the count.

---

## Scenario 7 — Application log monitoring

You are deploying a new application version and want to continuously watch new log entries.

### Your task

What command would you use?

What would you look for?

---

## Scenario 8 — Service restart verification

The manager says:

> "Restart nginx and confirm that it actually came back."

### Your task

Write the steps/commands.

Include a verification step.

---

# Part 3 — Intermediate Scenarios

## Scenario 9 — Restart only if the service is down

For:

```text
employee-portal
```

Requirement:

> If already running, do nothing.
>
> If stopped, start it.
>
> Print what happened.

### Your task

Write the script.

---

## Scenario 10 — Disk cleanup investigation

A production server reports:

```text
Disk usage: 94%
```

Your manager says:

> "Do not delete anything yet. First find out what is consuming the disk."

### Your task

Write the troubleshooting sequence.

Show:

```text
filesystem
→ directory
→ subdirectory
→ large files
→ logs/application data
```

---

## Scenario 11 — Large log files

Find files larger than `1G` under:

```text
/var/log
```

### Your task

Write the command.

Explain why searching `/var/log` can be better than immediately searching `/`.

---

## Scenario 12 — Application configuration check

Check whether:

```text
/etc/employee-portal/app.conf
```

exists.

If yes:

```text
Configuration found
```

Otherwise:

```text
Configuration missing
```

### Your task

Write the script.

---

## Scenario 13 — Backup before change

Before modifying:

```text
/etc/nginx/nginx.conf
```

you must create a backup.

### Your task

Write a sequence that:

1. Checks the file exists
2. Creates a backup
3. Prints the backup location

---

## Scenario 14 — Timestamped backup

Improve Scenario 13.

Create a backup similar to:

```text
nginx.conf.backup-2026-08-13-1650
```

### Your task

Write the approach/script.

---

## Scenario 15 — Check multiple directories

Check disk usage for:

```text
/var/log
/opt
/home
/data
```

### Your task

Use a loop.

---

## Scenario 16 — Retry application startup

An application may take several seconds to start.

Requirement:

> Check every 5 seconds and stop checking once it becomes active.

### Your task

Write a `while` loop.

---

## Scenario 17 — Retry with a maximum

Improve Scenario 16:

- Check every 5 seconds
- Maximum 6 attempts
- Active → success
- Still down after 6 attempts → failure

### Your task

Write the script or explain your logic.

---

# Part 4 — Script Arguments

## Scenario 18 — Generic service checker

Run:

```bash
./check-service.sh nginx
```

or:

```bash
./check-service.sh redis
```

### Your task

Write a script that uses `$1` as the service name.

---

## Scenario 19 — Missing argument

Someone runs:

```bash
./check-service.sh
```

The script should display:

```text
Usage: ./check-service.sh <service-name>
```

### Your task

Design the validation.

---

## Scenario 20 — Environment argument

Run:

```bash
./deploy.sh employee-portal production
```

The script should:

- Read application from `$1`
- Read environment from `$2`
- Print both
- Refuse to continue if either is missing

### Your task

Write the script/logic.

---

# Part 5 — Production-Style Scenarios

## Scenario 21 — Production vs development safety

Run:

```bash
./restart-service.sh payment-service production
```

Requirement:

> In production, ask for confirmation before restarting.
>
> In development, restart without asking.

### Your task

Use:

- `$1`
- `$2`
- `if`
- user input
- `systemctl`

---

## Scenario 22 — Health-check all services

Check:

```text
nginx
redis
payment-service
employee-portal
```

Output should identify each service and whether it is running.

### Your task

Use:

- variable/list
- loop
- condition

---

## Scenario 23 — Generate a health report

Write the following to:

```text
server-health.log
```

```text
===== SERVER HEALTH =====
Date:
Hostname:
Uptime:
Memory:
Disk:
Application status:
=========================
```

### Your task

Use command substitution and redirection.

---

## Scenario 24 — Health check with exit codes

Your script should:

- Return `0` if everything is healthy
- Return non-zero if disk is critical
- Return non-zero if the application is down

### Your task

Use `exit` and explain how `$?` can verify the result.

---

## Scenario 25 — Log monitoring script

Check the application log every 30 seconds.

If it contains `CRITICAL`, print an alert.

### Your task

Design the script using:

- `while`
- `sleep`
- `grep`
- exit status

---

# Part 6 — Automation + Cron

## Scenario 26 — Daily backup

Run:

```text
/opt/scripts/backup.sh
```

every day at 2:00 AM.

### Your task

Write the cron entry.

---

## Scenario 27 — Every 5 minutes

Run:

```text
/opt/scripts/check-payment.sh
```

every 5 minutes.

### Your task

Write the cron entry.

---

## Scenario 28 — Cron logging

Send all normal output and errors from:

```text
check-payment.sh
```

to:

```text
/var/log/payment-check.log
```

### Your task

Write the cron entry.

---

## Scenario 29 — Cron job didn't run

You are given:

```cron
*/5 * * * * /opt/scripts/check.sh
```

### Your task

Write your troubleshooting process.

Think about:

- Is the job installed?
- Is cron/crond running?
- Is the script executable?
- Does the script work manually?
- Does it use absolute paths?
- Where are cron logs?

---

# Part 7 — Debugging and Improvement

## Scenario 30 — Works manually but not in cron

A script works with:

```bash
./backup.sh
```

but fails from cron.

### Your task

List possible reasons.

Give multiple possibilities.

---

## Scenario 31 — Permission problem

A script works for you but fails for another user.

### Your task

Explain what you would investigate.

Think about:

- ownership
- permissions
- user
- groups
- paths
- sudo

---

## Scenario 32 — Script silently fails

A script runs but nobody knows whether it succeeded.

### Your task

Improve it so it:

- prints useful messages
- records output
- checks command success
- reports failures clearly

---

## Scenario 33 — Dangerous automation

You see:

```bash
rm -rf "$DIRECTORY"
```

in a production script.

### Your task

What could go wrong?

What checks would you add before allowing it?

---

## Scenario 34 — Infinite loop

A script contains:

```bash
while true
do
    systemctl status payment-service
done
```

### Your task

What is wrong?

How would you improve it?

---

# Part 8 — Think Like a DevOps Engineer

## Scenario 35 — Don't immediately fix

The manager says:

> "The payment service is down."

Before restarting anything, write the investigation sequence.

Think about:

```text
Service
Process
Logs
Disk
Memory
Configuration
Dependencies
```

---

## Scenario 36 — Automated incident checker

Create a script that checks:

1. Hostname
2. Uptime
3. Disk usage
4. Memory
5. Payment service status
6. Recent payment-service errors

### Your task

You don't need perfect syntax.

Write the structure/commands you would use.

---

## Scenario 37 — Multiple servers

You have:

```text
app01
app02
app03
app04
```

Check whether the payment service is running on all four.

### Your task

Design the automation.

If you don't remember SSH syntax, write the steps.

---

## Scenario 38 — Deployment verification

A deployment has just finished.

Verify:

- Service is running
- Application responds
- Disk is not critically full
- No obvious `ERROR` entries appeared in recent logs

### Your task

Write the verification process.

---

# Part 9 — Final Challenge

## Scenario 39 — Production deployment helper

Create:

```text
deploy-check.sh
```

It receives:

```bash
./deploy-check.sh <service> <environment>
```

Example:

```bash
./deploy-check.sh payment-service production
```

Requirements:

1. Validate both arguments.
2. Display application and environment.
3. If environment is `production`, ask for confirmation.
4. Check disk usage.
5. Refuse to continue if disk usage is 90% or higher.
6. Check that the service exists/is recognized by systemd.
7. Restart the service.
8. Wait for it to become active.
9. Retry up to 6 times.
10. Check recent service logs.
11. Print final SUCCESS or FAILURE.
12. Return an appropriate exit code.

### Your task

Design the complete script.

You are **not expected to know every command immediately**.

If you don't know something, write:

```text
Step 1: I would check ______
Step 2: I would use ______
Step 3: I would verify ______
```

That is a valid answer.

---

# Part 10 — Thinking Test

For these, do **not** write commands first.

Write your troubleshooting logic.

## Q40

> Server disk is 95% full.

What would you investigate?

```text
1.
2.
3.
4.
5.
```

---

## Q41

> Application is not responding.

What would you investigate?

```text
1.
2.
3.
4.
5.
```

---

## Q42

> Script works manually but not through cron.

What would you investigate?

```text
1.
2.
3.
4.
5.
```

---

## Q43

> User says "permission denied."

What would you investigate?

```text
1.
2.
3.
4.
5.
```

---

## Q44

> A process is consuming 100% CPU.

What would you investigate?

```text
1.
2.
3.
4.
5.
```

---

## Q45

> A service keeps stopping after being restarted.

What would you investigate?

```text
1.
2.
3.
4.
5.
```

---

# Final Thinking Challenge

For each problem, explain **how you think**, not just the command.

## A — "The server is slow."

```text
My thought process:

1.
2.
3.
4.
5.
```

## B — "The application is down."

```text
My thought process:

1.
2.
3.
4.
5.
```

## C — "The disk is full."

```text
My thought process:

1.
2.
3.
4.
5.
```

## D — "The cron job didn't execute."

```text
My thought process:

1.
2.
3.
4.
5.
```

---

# Practice Rule

For production-style questions, try to follow:

```text
UNDERSTAND
    ↓
INVESTIGATE
    ↓
CHECK
    ↓
CHANGE
    ↓
VERIFY
    ↓
DOCUMENT
```

Especially in production, avoid jumping straight to:

```bash
kill -9
rm -rf
chmod 777
reboot
```

without understanding the problem first.

The goal is not to memorize 100 commands.

The goal is to develop this mindset:

> **What is the problem? What evidence do I need? Which command gives me that evidence? What does the output mean? What is the safest fix? How do I verify the fix?**
