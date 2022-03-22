# 45个GIT经典操作场景，专治不会合代码

下边我们整理了45个日常用git合代码的经典操作场景，基本覆盖了工作中的需求。  

## 一、提交(commit)

### 1. 我刚才提交了什么?

如果你用`git commit -a` 提交了一次变化(changes)，而你又不确定到底这次提交了哪些内容。你就可以用下面的命令显示当前`HEAD`上的最近一次的提交(commit)：

```git
(main)$ git show
```

或者

```git
$ git log -n1 -p
```

### 2. 我的提交信息(commit message)写错了

如果你的提交信息(commit message)写错了且这次提交(commit)还没有推(push)，你可以通过下面的方法来修改提交信息(commit message)：

```git
$ git commit --amend --only
```

这会打开你的默认编辑器，在这里你可以编辑信息。另一方面，你也可以用一条命令一次完成：

```git
$ git commit --amend --only -m 'xxxxxxx'
```

如果你已经推(push)了这次提交(commit)，你可以修改这次提交(commit)然后强推(force push)， 但是不推荐这么做。

### 3. 我提交(commit)里的用户名和邮箱不对

如果这只是单个提交(commit)，修改它：

```git
$ git commit --amend --author "New Authorname <authoremail@mydomain.com>"
```

如果你需要修改所有历史，参考 'git filter-branch'的指南页。

### 4. 我想从一个提交(commit)里移除一个文件

通过下面的方法，从一个提交(commit)里移除一个文件：

```git
$ git checkout HEAD^ myfile
$ git add -A
$ git commit --amend
```

这将非常有用，当你有一个开放的补丁(open patch)，你往上面提交了一个不必要的文件，你需要强推(force push)去更新这个远程补丁。

### 5. 我想删除我的的最后一次提交(commit)

如果你需要删除推了的提交(pushed commits)，你可以使用下面的方法。可是，这会不可逆的改变你的历史，也会搞乱那些已经从该仓库拉取(pulled)了的人的历史。简而言之，如果你不是很确定，千万不要这么做。

```git
$ git reset HEAD^ --hard
$ git push -f [remote] [branch]
```

如果你还没有推到远程，把Git重置(reset)到你最后一次提交前的状态就可以了(同时保存暂存的变化)：

```git
(my-branch*)$ git reset --soft HEAD@{1}
```

这只能在没有推送之前有用。如果你已经推了，唯一安全能做的是 `git revert SHAofBadCommit`， 那会创建一个新的提交(commit)用于撤消前一个提交的所有变化(changes)；或者，如果你推的这个分支是rebase-safe的 (例如：其它开发者不会从这个分支拉)，只需要使用 `git push -f`。

### 6. 删除任意提交(commit)

同样的警告：不到万不得已的时候不要这么做

```git
$ git rebase --onto SHA1_OF_BAD_COMMIT^ SHA1_OF_BAD_COMMIT
$ git push -f [remote] [branch]
```

### 7. 我尝试推一个修正后的提交(amended commit)到远程，但是报错

```git
To https://github.com/yourusername/repo.git
! [rejected]        mybranch -> mybranch (non-fast-forward)
error: failed to push some refs to 'https://github.com/hongyidashi/collect.git.org.git'
hint: Updates were rejected because the tip of your current branch is behind
hint: its remote counterpart. Integrate the remote changes (e.g.
hint: 'git pull ...') before pushing again.
hint: See the 'Note about fast-forwards' in 'git push --help' for details.
```

注意，rebasing(见下面)和修正(amending)会用一个**新的提交(commit)代替旧的**，所以如果之前你已经往远程仓库上推过一次修正前的提交(commit)，那你现在就必须强推(force push) (`-f`)。注意 – *总是* 确保你指明一个分支!

```git
(my-branch)$ git push origin mybranch -f
```

一般来说, **要避免强推**. 最好是创建和推(push)一个新的提交(commit)，而不是强推一个修正后的提交。后者会使那些与该分支或该分支的子分支工作的开发者，在源历史中产生冲突。

### 8. 我意外的做了一次硬重置(hard reset)，我想找回我的内容

如果你意外的做了 `git reset --hard`，你通常能找回你的提交(commit)，因为Git对每件事都会有日志，且都会保存几天。

```git
(main)$ git reflog
```

你将会看到一个你过去提交(commit)的列表，和一个重置的提交。选择你想要回到的提交(commit)的SHA，再重置一次：

```git
(main)$ git reset --hard SHA1234
```

## 二、暂存(Staging)

### 9. 我需要把暂存的内容添加到上一次的提交(commit)

```git
(my-branch*)$ git commit --amend
```

### 10. 我想要暂存一个新文件的一部分，而不是这个文件的全部

一般来说，如果你想暂存一个文件的一部分，你可这样做:

```git
$ git add --patch filename.x
```

`-p` 简写。这会打开交互模式，你将能够用 `s` 选项来分隔提交(commit)；然而，如果这个文件是新的，会没有这个选择，添加一个新文件时，这样做：

```git
$ git add -N filename.x
```

然后, 你需要用 `e` 选项来手动选择需要添加的行，执行 `git diff --cached` 将会显示哪些行暂存了哪些行只是保存在本地了。

### 11. 我想把在一个文件里的变化(changes)加到两个提交(commit)里

`git add` 会把整个文件加入到一个提交。`git add -p` 允许交互式的选择你想要提交的部分。

### 12. 我想把暂存的内容变成未暂存，把未暂存的内容暂存起来

多数情况下，你应该将所有的内容变为未暂存，然后再选择你想要的内容进行commit。但假定你就是想要这么做，这里你可以创建一个临时的commit来保存你已暂存的内容，然后暂存你的未暂存的内容并进行stash。然后reset最后一个commit将原本暂存的内容变为未暂存，最后stash pop回来。

```git
$ git commit -m "WIP"
$ git add .
$ git stash
$ git reset HEAD^
$ git stash pop --index 0
```

注意1：这里使用`pop`仅仅是因为想尽可能保持幂等。

注意2：假如你不加上`--index`你会把暂存的文件标记为为存储。

## 三、未暂存(Unstaged)的内容

### 13. 我想把未暂存的内容移动到一个新分支

```git
git checkout -b my-branch
```

### 14. 我想把未暂存的内容移动到另一个已存在的分支

```git
$ git stash
$ git checkout my-branch
$ git stash pop
```

### 15. 我想丢弃本地未提交的变化(uncommitted changes)

如果你只是想重置源(origin)和你本地(local)之间的一些提交(commit)，你可以：

```git
# one commit
(my-branch)$ git reset --hard HEAD^
# two commits
(my-branch)$ git reset --hard HEAD^^
# four commits
(my-branch)$ git reset --hard HEAD~4
# or
(main)$ git checkout -f
```

重置某个特殊的文件，你可以用文件名做为参数：

```git
$ git reset filename
```

### 16. 我想丢弃某些未暂存的内容

如果你想丢弃工作拷贝中的一部分内容，而不是全部。

签出(checkout)不需要的内容，保留需要的。

```git
$ git checkout -p
# Answer y to all of the snippets you want to drop
```

另外一个方法是使用 `stash`， Stash所有要保留下的内容，重置工作拷贝，重新应用保留的部分。

```git
$ git stash -p
# Select all of the snippets you want to save
$ git reset --hard
$ git stash pop
```

或者，stash 你不需要的部分，然后stash drop。

```git
$ git stash -p
# Select all of the snippets you don't want to save
$ git stash drop
```

## 四、分支(Branches)

### 17. 我从错误的分支拉取了内容，或把内容拉取到了错误的分支

这是另外一种使用 `git reflog` 情况，找到在这次错误拉(pull) 之前HEAD的指向。

```git
(main)$ git reflog
ab7555f HEAD@{0}: pull origin wrong-branch: Fast-forward
c5bc55a HEAD@{1}: checkout: checkout message goes here
```

重置分支到你所需的提交(desired commit)：

```git
$ git reset --hard c5bc55a
```

### 18. 我想扔掉本地的提交(commit)，以便我的分支与远程的保持一致

先确认你没有推(push)你的内容到远程。

`git status` 会显示你领先(ahead)源(origin)多少个提交：

```git
(my-branch)$ git status
# On branch my-branch
# Your branch is ahead of 'origin/my-branch' by 2 commits.
#   (use "git push" to publish your local commits)
```

一种方法是：

```git
(main)$ git reset --hard origin/my-branch
```

### 19. 我需要提交到一个新分支，但错误的提交到了main

在main下创建一个新分支，不切换到新分支，仍在main下：

```git
(main)$ git branch my-branch
```

把main分支重置到前一个提交：

```git
(main)$ git reset --hard HEAD^
```

`HEAD^` 是 `HEAD^1` 的简写，你可以通过指定要设置的`HEAD`来进一步重置。

或者, 如果你不想使用 `HEAD^`，找到你想重置到的提交(commit)的hash(`git log` 能够完成)， 然后重置到这个hash。使用`git push` 同步内容到远程。

例如，main分支想重置到的提交的hash为`a13b85e`:

```git
(main)$ git reset --hard a13b85e
HEAD is now at a13b85e
```

签出(checkout)刚才新建的分支继续工作：

```git
(main)$ git checkout my-branch
```

### 20. 我想保留来自另外一个ref-ish的整个文件

假设你正在做一个原型方案(原文为working spike)， 有成百的内容，每个都工作得很好。现在，你提交到了一个分支，保存工作内容:

```git
(solution)$ git add -A && git commit -m "Adding all changes from this spike into one big commit."
```

当你想要把它放到一个分支里 (可能是`feature`, 或者 `develop`)，你关心是保持整个文件的完整，你想要一个大的提交分隔成比较小。

假设你有:

- 分支 `solution`，拥有原型方案，领先 `develop` 分支。

- 分支 `develop`，在这里你应用原型方案的一些内容。

我去可以通过把内容拿到你的分支里，来解决这个问题:

```git
(develop)$ git checkout solution -- file1.txt
```

这会把这个文件内容从分支 `solution` 拿到分支 `develop` 里来：

```git
# On branch develop
# Your branch is up-to-date with 'origin/develop'.
# Changes to be committed:
#  (use "git reset HEAD <file>..." to unstage)
#
#        modified:   file1.txt
```

然后，正常提交。

### 21. 我把几个提交(commit)提交到了同一个分支，而这些提交应该分布在不同的分支里

假设你有一个`main`分支， 执行`git log`，你看到你做过两次提交：

```git
(main)$ git log

commit e3851e817c451cc36f2e6f3049db528415e3c114
Author: Alex Lee <alexlee@example.com>
Date:   Tue Jul 22 15:39:27 2014 -0400

    Bug #21 - Added CSRF protection

commit 5ea51731d150f7ddc4a365437931cd8be3bf3131
Author: Alex Lee <alexlee@example.com>
Date:   Tue Jul 22 15:39:12 2014 -0400

    Bug #14 - Fixed spacing on title

commit a13b85e984171c6e2a1729bb061994525f626d14
Author: Aki Rose <akirose@example.com>
Date:   Tue Jul 21 01:12:48 2014 -0400

    First commit
```

让我们用提交hash(commit hash)标记bug (`e3851e8` for #21, `5ea5173` for #14)。

首先，我们把`main`分支重置到正确的提交(`a13b85e`)：

```git
(main)$ git reset --hard a13b85e
HEAD is now at a13b85e
```

现在，我们对 bug #21 创建一个新的分支:

```git
(main)$ git checkout -b 21
```

接着，我们用 *cherry-pick* 把对bug #21的提交放入当前分支。这意味着我们将应用(apply)这个提交(commit)，仅仅这一个提交(commit)，直接在HEAD上面。

```git
(21)$ git cherry-pick e3851e8
```

这时候，这里可能会产生冲突， 参见交互式 rebasing 章 **冲突节** 解决冲突.

再者，我们为bug #14 创建一个新的分支，也基于`main`分支

```git
(21) git checkout main
(main)$ git checkout -b 14
```

最后，为 bug #14 执行 `cherry-pick`：

```git
(14)$ git cherry-pick 5ea5173
```

### 22. 我想删除上游(upstream)分支被删除了的本地分支

一旦你在github 上面合并(merge)了一个pull request，你就可以删除你fork里被合并的分支。如果你不准备继续在这个分支里工作，删除这个分支的本地拷贝会更干净，使你不会陷入工作分支和一堆陈旧分支的混乱之中。

```git
$ git fetch -p
```

### 23. 我不小心删除了我的分支

如果你定期推送到远程，多数情况下应该是安全的，但有些时候还是可能删除了还没有推到远程的分支。让我们先创建一个分支和一个新的文件：

```git
(main)$ git checkout -b my-branch
(my-branch)$ git branch
(my-branch)$ touch foo.txt
(my-branch)$ ls
README.md foo.txt
```

添加文件并做一次提交

```git
(my-branch)$ git add .
(my-branch)$ git commit -m 'foo.txt added'
(my-branch)$ foo.txt added
 1 files changed, 1 insertions(+)
 create mode 100644 foo.txt
(my-branch)$ git log

commit 4e3cd85a670ced7cc17a2b5d8d3d809ac88d5012
Author: siemiatj <siemiatj@example.com>
Date:   Wed Jul 30 00:34:10 2014 +0200

    foo.txt added

commit 69204cdf0acbab201619d95ad8295928e7f411d5
Author: Kate Hudson <katehudson@example.com>
Date:   Tue Jul 29 13:14:46 2014 -0400

    Fixes #6: Force pushing after amending commits
```

现在我们切回到主(main)分支，“不小心的”删除`my-branch`分支

```git
(my-branch)$ git checkout main
Switched to branch 'main'
Your branch is up-to-date with 'origin/main'.
(main)$ git branch -D my-branch
Deleted branch my-branch (was 4e3cd85).
(main)$ echo oh noes, deleted my branch!
oh noes, deleted my branch!
```

在这时候你应该想起了`reflog`, 一个升级版的日志，它存储了仓库(repo)里面所有动作的历史。

```git
(main)$ git reflog
69204cd HEAD@{0}: checkout: moving from my-branch to main
4e3cd85 HEAD@{1}: commit: foo.txt added
69204cd HEAD@{2}: checkout: moving from main to my-branch
```

正如你所见，我们有一个来自删除分支的提交hash(commit hash)，接下来看看是否能恢复删除了的分支。

```git
(main)$ git checkout -b my-branch-help
Switched to a new branch 'my-branch-help'
(my-branch-help)$ git reset --hard 4e3cd85
HEAD is now at 4e3cd85 foo.txt added
(my-branch-help)$ ls
README.md foo.txt
```

看！我们把删除的文件找回来了。Git的 `reflog` 在rebasing出错的时候也是同样有用的。