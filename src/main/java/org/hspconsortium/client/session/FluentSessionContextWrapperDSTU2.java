/*
 * #%L
 * Health Services Platform Consortium - HSPC Client
 * %%
 * Copyright (C) 2014 - 2015 Healthcare Services Platform Consortium
 * %%
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *      http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 * #L%
 */
package org.hspconsortium.client.session;

/**
 * @deprecated Use SessionContextWrapperDSTU2 instead
 */
@Deprecated
public class FluentSessionContextWrapperDSTU2 extends SessionContextWrapperDSTU2 {
    public FluentSessionContextWrapperDSTU2(Session session) {
        super(session);
    }

    public FluentSessionContextWrapperDSTU2(Session session, boolean localizeClaimUrl) {
        super(session, localizeClaimUrl);
    }
}
