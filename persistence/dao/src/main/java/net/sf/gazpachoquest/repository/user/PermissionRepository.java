/*******************************************************************************
 * Copyright (c) 2014 antoniomariasanchez at gmail.com. All rights reserved. This program and the accompanying materials
 * are made available under the terms of the GNU Public License v3.0 which accompanies this distribution, and is
 * available at http://www.gnu.org/licenses/gpl.html
 * 
 * Contributors: antoniomaria - initial API and implementation
 ******************************************************************************/
package net.sf.gazpachoquest.repository.user;

import net.sf.gazpachoquest.domain.user.Permission;
import net.sf.gazpachoquest.repository.support.GenericRepository;

public interface PermissionRepository extends GenericRepository<Permission> {

    // @Query("select p from Permission p left join p.role r left join r.users u where u.id = :userId")
    // List<Permission> findByUser(@Param("userId")
    // Integer userId);

}