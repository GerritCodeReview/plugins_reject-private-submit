// Copyright (C) 2018 The Android Open Source Project
//
// Licensed under the Apache License, Version 2.0 (the "License");
// you may not use this file except in compliance with the License.
// You may obtain a copy of the License at
//
// http://www.apache.org/licenses/LICENSE-2.0
//
// Unless required by applicable law or agreed to in writing, software
// distributed under the License is distributed on an "AS IS" BASIS,
// WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
// See the License for the specific language governing permissions and
// limitations under the License.

package com.googlesource.gerrit.plugins.rejectprivate;

import com.google.gerrit.extensions.registration.DynamicSet;
import com.google.gerrit.reviewdb.client.Branch.NameKey;
import com.google.gerrit.reviewdb.client.PatchSet.Id;
import com.google.gerrit.server.IdentifiedUser;
import com.google.gerrit.server.git.CodeReviewCommit;
import com.google.gerrit.server.git.validators.MergeValidationException;
import com.google.gerrit.server.git.validators.MergeValidationListener;
import com.google.gerrit.server.project.ProjectState;
import com.google.inject.AbstractModule;
import com.google.inject.Singleton;
import org.eclipse.jgit.lib.Repository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Singleton
public class RejectPrivateSubmitListener implements MergeValidationListener {

  private static final Logger log = LoggerFactory.getLogger(RejectPrivateSubmitListener.class);

  public static class Module extends AbstractModule {
    @Override
    protected void configure() {
      DynamicSet.bind(binder(), MergeValidationListener.class)
          .to(RejectPrivateSubmitListener.class);
    }
  }

  @Override
  public void onPreMerge(
      Repository repo,
      CodeReviewCommit commit,
      ProjectState destProject,
      NameKey destBranch,
      Id patchSetId,
      IdentifiedUser caller)
      throws MergeValidationException {
    if (commit == null || commit.change() == null) {
      log.warn(
          "NULL commit or change in listener while submitting to {} on {} by {}: {}",
          destProject,
          destBranch,
          caller,
          commit);
      return;
    }
    if (commit.change().isPrivate()) {
      throw new MergeValidationException("Private changes may not be submitted");
    }
  }
}
